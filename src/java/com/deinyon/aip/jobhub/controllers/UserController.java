/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.database.UserDAO;
import com.deinyon.aip.jobhub.users.User;
import java.io.IOException;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "userController")
@SessionScoped
public class UserController
{
    public User getActingUser() throws IOException
    {
        // Obtain the username of the current principal
        String username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        
        // If null, there is no logged-in user
        if(username == null)
            return null;
        
        // Otherwise, find the user in the database
        try(UserDAO dao = new UserDAO())
        {
            return dao.find(username);
        }
    }
    
    public String logOut()
    {
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
}
