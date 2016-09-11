package com.deinyon.aip.jobhub.controllers;

import java.io.Serializable;

/**
 * Defines the core functionality of a Controller Bean.
 * Practically, this abstract class exposes a recyclable instance
 * of a User Controller to extended Controllers.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public abstract class GenericController implements Serializable
{
    /**
     * A recycled instance of a User Controller, which may be
     * used to query information about the presently logged-in user.
     */
    private UserController userController;
    
    /**
     * @return A recycled instance of a User Controller, which may be
     * used to query information about the presently logged-in user.
     */
    protected UserController getUserController()
    {
        if(this.userController == null)
            return this.userController = new UserController();
        return this.userController;
    }
}
