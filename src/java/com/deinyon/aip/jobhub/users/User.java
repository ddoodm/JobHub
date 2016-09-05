/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.users;

import java.util.UUID;


public abstract class User
{
    private String username, email;
    private String surname, givenName, company;
    private String bio;
    
    public User() {}

    public User(String username, String email, String surname, String givenName, String company) {
        this.username = username;
        this.email = email;
        this.surname = surname;
        this.givenName = givenName;
        this.company = company;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getFullName()
    {
        return String.format("%s %s", givenName, surname);
    }
    
    public String getDisplayName()
    {
        if(company != null && !company.equals(""))
            return company;
        return getFullName();
    }
}
