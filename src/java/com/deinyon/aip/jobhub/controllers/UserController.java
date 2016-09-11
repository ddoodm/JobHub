/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.database.UserClassification;
import com.deinyon.aip.jobhub.database.UserDAO;
import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * A Controller Bean which is used by many components of the application to
 * retrieve details about the presently logged-in user.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@ManagedBean(name = "userController")
@RequestScoped
public class UserController implements Serializable
{
    /**
     * A cached instance of the presently active user, which prevents unnecessary
     * database transactions when accessing the user from multiple controller
     * instances.
     */
    private User cachedUser;
    
    /**
     * @return The presently logged-in user. Null if there is no logged-in user.
     * @throws IOException If a database error prevented the user from being accessed.
     */
    public User getActingUser() throws IOException
    {
        // If the user has been retrieved already in this request, return the cached instance
        if(cachedUser != null)
            return cachedUser;
        
        // Obtain the username of the current principal
        String username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        
        // If null, there is no logged-in user
        if(username == null)
            return null;
        
        // Otherwise, find the user in the database
        try(UserDAO dao = new UserDAO())
        {
            return cachedUser = dao.find(username);
        }
    }
    
    /**
     * @param <T> The type of User to return (Employee or Employer)
     * @return The presently logged-in user, casted to the specified type.
     * @throws IOException If a database error prevented the user from being accessed.
     */
    public <T extends User> T getActingUserTyped() throws IOException
    {
        User user = getActingUser();
        return (T)user;
    }
    
    /**
     * Logs the user out of the Container Security platform.
     * @return The outcome to which the user should be directed.
     */
    public String logOut()
    {
        // Do not cache this user any longer
        this.cachedUser = null;
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        
        try
        {
            request.logout();
        }
        catch (ServletException e)
        {
            // The user could not be logged-out
            System.out.println(e);
            return "";
        }
        
        return "home";
    }
    
    /**
     * @param job The job which could have been created by the active user
     * @return True if the presently active user created the specified job
     * @throws IOException If a database error prevented the system from
     * gathering details about the user.
     */
    public boolean userCreatedJob(Job job) throws IOException
    {
        User user = getActingUser();
        return job.wasCreatedBy(user);
    }
    
    /**
     * @return True if the presently active user is an employee.
     * @throws IOException If a database error prevented the system from
     * gathering details about the user.
     */
    public boolean isUserEmployee() throws IOException
    {
        return getActingUser() instanceof Employee;
    }
    
    /**
     * @return True if the presently active user is an employer.
     * @throws IOException If a database error prevented the system from
     * gathering details about the user.
     */
    public boolean isUserEmployer() throws IOException
    {
        return getActingUser() instanceof Employer;
    }
}
