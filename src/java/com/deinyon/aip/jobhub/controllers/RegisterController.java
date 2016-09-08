/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.database.UserClassification;
import com.deinyon.aip.jobhub.database.UserDAO;
import com.deinyon.aip.jobhub.users.*;
import com.deinyon.aip.jobhub.util.ShaHash;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.*;
import org.hibernate.validator.constraints.*;

@ManagedBean(name = "registerController")
@SessionScoped
public class RegisterController implements Serializable
{
    private User user;
    
    /**
     * The type of user that should be created.
     * Where the type has not been specified, we default to Employee types
     */
    private UserClassification userClassifier = UserClassification.Employee;
    
    public void signUp() throws IOException
    {
        try(UserDAO dao = new UserDAO())
        {
            dao.save(user);
        }
    }
    
    public void initializeUser()
    {
        // Determine the type of the user
        switch(userClassifier)
        {
            default:
            case Employee:  user = new Employee(); break;
            case Employer:  user = new Employer(); break;
        }
    }
    
    public User getUser()
    {
        return user;
    }
    
    @NotEmpty
    public String getUserType()
    {
        return userClassifier.name();
    }
    
    public void setUserType(String userType)
    {
        userClassifier = UserClassification.valueOf(userType);
    }
    
    @Length(min = 6, message = "Please write at least a 6-character passwrod")
    public String getPlaintextPassword()
    {
        return "";
    }

    public void setPlaintextPassword(String plaintextPassword) throws IllegalStateException
    {
        // Here, we intercept the plaintext passowrd so that
        // we can generate and store the hash of the password instead
        ShaHash password = new ShaHash(plaintextPassword);
        user.setPassword(password);
    }
}
