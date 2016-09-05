/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.users;

/**
 *
 * @author Ddoodm
 */
public class Employer extends User
{
    public Employer() {
    }

    public Employer(String username, String email, String surname, String givenName, String company) {
        super(username, email, surname, givenName, company);
    }
}
