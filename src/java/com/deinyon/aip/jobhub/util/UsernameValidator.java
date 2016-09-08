/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.util;

import com.deinyon.aip.jobhub.database.UserDAO;
import com.deinyon.aip.jobhub.users.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validates a username by querying to Users database
 * to determine whether the username already exists
 */
@FacesValidator("com.deinyon.aip.jobhub.UrlValidator")
public class UsernameValidator implements Validator
{
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        if(!(value instanceof String))
            return;
        
        String username = (String)value;
        
        try(UserDAO userDao = new UserDAO())
        {
            User user = userDao.find(username);
            
            if(user != null)
                throw new ValidatorException(
                    new FacesMessage("That username is already taken. Please enter another."));
        }
        catch (IOException ex)
        {
            // In this case, a database error occurred
            throw new ValidatorException(
                    new FacesMessage("The username could not be validated because of a database error"),
                    ex);
        }
    }
}
