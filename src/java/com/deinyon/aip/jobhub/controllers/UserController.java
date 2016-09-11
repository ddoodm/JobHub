/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.database.UserClassification;
import com.deinyon.aip.jobhub.database.UserDAO;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import java.io.IOException;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "userController")
@RequestScoped
public class UserController
{
    private static User cachedUser;
    
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
    
    public <T extends User> T getActingUserTyped() throws IOException
    {
        User user = getActingUser();
        return (T)user;
    }
    
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
            System.out.println(e);
            return "";
        }
        
        return "home";
    }
    
    public boolean userCreatedJob(Job job) throws IOException
    {
        User user = getActingUser();
        
        // If the user is not an employer, they could not have created the job
        if(user == null || !(user instanceof Employer))
            return false;
        
        return job.getEmployer().equals(user);
    }
    
    public boolean isUserEmployee() throws IOException
    {
        User user = getActingUser();
        if(user == null)
            return false;
        
        return user.getClassifier() == UserClassification.Employee;
    }
    
    public boolean isUserEmployer() throws IOException
    {
        User user = getActingUser();
        if(user == null)
            return false;
        
        return user.getClassifier() == UserClassification.Employer;
    }
}
