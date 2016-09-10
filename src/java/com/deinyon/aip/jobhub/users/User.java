/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.users;

import com.deinyon.aip.jobhub.database.UserClassification;
import com.deinyon.aip.jobhub.util.ShaHash;
import java.util.Objects;
import org.hibernate.validator.constraints.*;

public abstract class User
{
    private String username, email;
    private String surname, givenName, company;
    private String bio;
    private ShaHash password;
    
    public User() {}

    public User(String username, ShaHash password, String email, String surname, String givenName, String company) {
        this.username = username;
        this.email = email;
        this.surname = surname;
        this.givenName = givenName;
        this.company = company;
        this.password = password;
    }

    /**
     * @return The user's classification identifier, determined by the instance type
     */
    public abstract UserClassification getClassifier();
    
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof User))
            return false;
        
        return ((User)other).getUsername().equals(this.getUsername());
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.username);
        hash = 31 * hash + Objects.hashCode(this.email);
        hash = 31 * hash + Objects.hashCode(this.surname);
        hash = 31 * hash + Objects.hashCode(this.givenName);
        return hash;
    }
    
    @Length(min = 2, max = 64, message = "Please enter a meaningful username below 64 characters")
    @NotEmpty(message = "Please enter a username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty(message = "Please enter your E-Mail address")
    @Email(message = "Please enter a well-formed E-Mail address")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty(message = "Please enter your last name")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @NotEmpty(message = "Please enter your first name")
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

    public ShaHash getPassword() {
        return password;
    }

    public void setPassword(ShaHash password) {
        this.password = password;
    }
    
    /**
     * @return the concatenation of the user's first and last name
     */
    public String getFullName()
    {
        return String.format("%s %s", givenName, surname);
    }
    
    /**
     * The display name is the user's full name, or their company name (if specified)
     * @return The user's name, as it should be displayed to others
     */
    public String getDisplayName()
    {
        if(company != null && !company.equals(""))
            return company;
        return getFullName();
    }
}
