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
            System.out.println(e);
            return null;
        }
        
        return "jobs";
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return plaintextPassword;
    }
    
    public void setPassword(String password)
    {
        this.plaintextPassword = password;
    }
}
