/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean(name = "loginController")
@RequestScoped
public class LoginController
{
    private String username, plaintextPassword;
    
    public String login()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        
        try
        {
            request.login(username, plaintextPassword);
        }
        catch (ServletException e)
        {
            // Report to the user that the login attempt failed
            context.addMessage(null, new FacesMessage("That username or password was incorrect. Try again."));
            return null;
        }
        
        return "jobs";
    }

    @NotEmpty(message = "Enter your username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @NotEmpty(message = "Enter your password")
    public String getPassword()
    {
        return plaintextPassword;
    }
    
    public void setPassword(String password)
    {
        this.plaintextPassword = password;
    }
}
