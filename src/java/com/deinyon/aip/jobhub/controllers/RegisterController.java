/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.users.User;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "registerController")
@RequestScoped
public class RegisterController implements Serializable
{
    private User user = new User();
    
    public void signUp()
    {
        
    }
    
    public User getUser()
    {
        return user;
    }
    
    public String getUserType()
    {
        return "";
    }
    
    public void setUserType(String userType)
    {
        
    }
    
    public String getPlaintextPassword()
    {
        return "";
    }
    
    public void setPlaintextPassword(String plaintextPassword)
    {
        
    }
}