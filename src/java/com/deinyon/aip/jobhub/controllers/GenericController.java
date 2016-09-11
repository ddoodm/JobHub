/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import java.io.Serializable;

public abstract class GenericController implements Serializable
{
    private UserController userController;
    
    protected UserController getUserController()
    {
        if(this.userController == null)
            return this.userController = new UserController();
        return this.userController;
    }
}
