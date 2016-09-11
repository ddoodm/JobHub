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

/**
 * The Controller Bean which interfaces the Registration page with the database,
 * and validation and business logic.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@ManagedBean(name = "registerController")
@SessionScoped
public class RegisterController implements Serializable
{
    /**
     * A new instance of a User which is being registered by this page.
     */
    private User user;
    
    /**
     * The type of user that should be created.
     * Where the type has not been specified, we default to Employee types
     */
    private UserClassification userClassifier = UserClassification.Employee;
    
    /**
     * Called when all form validation is successful. Records the new user into
     * the database, effectively registering the user.
     * @return The outcome to which the user should be directed.
     * @throws IOException If a database error prevented the user from being
     * registered.
     */
    public String signUp() throws IOException
    {
        try(UserDAO dao = new UserDAO())
        {
            dao.save(user);
        }
        
        // Clear the form and redirect to the Log In Facelet
        return "login";
    }
    
    /**
     * Prepares the active User instance given the type of user which should be
     * created.
     * @param userType The String representation of the UserClassification value
     * which denotes the type of user to instantiate.
     */
    public void initializeUser(String userType)
    {
        // When the classifier string is not specified, we use the default class
        if(userType != null)
        {
            try
            {
                userClassifier = UserClassification.valueOf(userType);
            }
            catch(Exception ex) { }
        }
        
        // Determine the type of the user
        switch(userClassifier)
        {
            default:
            case Employee:  user = new Employee(); break;
            case Employer:  user = new Employer(); break;
        }
    }
    
    /**
     * @return The user which is presently being registered
     */
    public User getUser()
    {
        return user;
    }
    
    /**
     * @return The String representation (name) of the UserClassification value
     * which denotes the type of user to register.
     */
    @NotEmpty
    public String getUserType()
    {
        return userClassifier.name();
    }
    
    /**
     * Specifies the String representation (name) of the UserClassification value
     * which denotes the type of user to register.
     * @param userType The String representation (name) of the UserClassification value
     * which denotes the type of user to register.
     */
    public void setUserType(String userType)
    {
        userClassifier = UserClassification.valueOf(userType);
    }
    
    /**
     * @return An empty String. The controller will not return the user's password
     * in plain-text over the network when registration fails.
     */
    @Length(min = 6, message = "Please write at least a 6-character passwrod")
    public String getPlaintextPassword()
    {
        return "";
    }

    /**
     * Takes a plain-text password and immediately hashes it so as to not retain
     * sensitive data.
     * @param plaintextPassword The user's password, in plain un-hashed text.
     * @throws IllegalStateException If the server does not have the capacity to
     * hash the password with the necessary hashing algorithm.
     */
    public void setPlaintextPassword(String plaintextPassword) throws IllegalStateException
    {
        // Here, we intercept the plaintext passowrd so that
        // we can generate and store the hash of the password instead
        ShaHash password = new ShaHash(plaintextPassword);
        user.setPassword(password);
    }
}
