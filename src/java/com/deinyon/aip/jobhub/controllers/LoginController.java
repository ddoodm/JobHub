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

/**
 * The Controller Bean which interfaces the Log-In Facelet with the Container
 * Security platform, and the User data store.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@ManagedBean(name = "loginController")
@RequestScoped
public class LoginController extends GenericController
{
    /**
     * The username and password of the User are required to identify and
     * authenticate the user.
     */
    private String username, plaintextPassword;
    
    /**
     * Using the specified Username and Password, logs the user in to the
     * Container Security platform.
     * @remarks The username and password must be set before calling this function.
     * @return The outcome to which the user should be redirected after log-in.
     * Null if the user could not be authenticated.
     */
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

    /**
     * @return The identifier (user-name) of the user who is to be authenticated.
     */
    @NotEmpty(message = "Enter your username")
    public String getUsername()
    {
        return username;
    }

    /**
     * Specifies the identifier (user-name) of the user who is to be authenticated.
     * @param username The identifier (user-name) of the user who is to be authenticated.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * @return An empty string. The User's password will never be conveyed back
     * over the network.
     */
    @NotEmpty(message = "Enter your password")
    public String getPassword()
    {
        return "";
    }
    
    /**
     * Specifies the password (in plain un-hashed text) that will be used to
     * authenticate the user.
     * @param password The password (in plain un-hashed text) that will be used to
     * authenticate the user.
     */
    public void setPassword(String password)
    {
        this.plaintextPassword = password;
    }
}
