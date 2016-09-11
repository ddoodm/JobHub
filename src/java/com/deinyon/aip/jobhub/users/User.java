package com.deinyon.aip.jobhub.users;

import com.deinyon.aip.jobhub.database.UserClassification;
import com.deinyon.aip.jobhub.util.ShaHash;
import java.util.Objects;
import org.hibernate.validator.constraints.*;

/**
 * Defines the base functionality of a JobHub user
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public abstract class User
{
    /**
     * The user-name (identifier) of this user
     */
    private String username;
    
    /**
     * The user's E-Mail address
     */
    private String email;
    
    /**
     * The user's surname (family name)
     */
    private String surname;
    
    /**
     * The user's given name (first name)
     */
    private String givenName;
    
    /**
     * The company with which this user is associated. If specified, 
     * the company name will be displayed to others in all cases, in place of the user's
     * first and last names.
     */
    private String company;
    
    /**
     * A short biographic message (not used).
     */
    private String bio;
    
    /**
     * The cryptographic hash of the user's password (should be null in most cases)
     */
    private ShaHash password;
    
    /**
     * Creates a new User
     */
    public User() {}

    /**
     * Creates a new User with known parameters
     * @param username The user-name (identifier) of this user
     * @param password The cryptographic hash of the user's password (should be null in most cases)
     * @param email The user's E-Mail address
     * @param surname The user's surname (family name)
     * @param givenName The user's given name (first name)
     * @param company The company with which this user is associated. If specified, 
     * the company name will be displayed to others in all cases, in place of the user's
     * first and last names.
     */
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
    
    /**
     * Performs comparison of Users by their username (identifier)
     * @param other An object which which to compare this User
     * @return True if the objects represent the same User
     */
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof User))
            return false;
        
        return ((User)other).getUsername().equals(this.getUsername());
    }

    /**
     * Generates a hash-code determined by static properties of the User
     * @return A hash which represents this User
     */
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
    
    /**
     * @return The user-name (identifier) of this user
     */
    @Length(min = 2, max = 64, message = "Please enter a meaningful username below 64 characters")
    @NotEmpty(message = "Please enter a username")
    public String getUsername() {
        return username;
    }

    /**
     * @param username The user-name (identifier) of this user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The user's E-Mail address
     */
    @NotEmpty(message = "Please enter your E-Mail address")
    @Email(message = "Please enter a well-formed E-Mail address")
    public String getEmail() {
        return email;
    }

    /**
     * @param email The user's E-Mail address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The user's surname (family name)
     */
    @NotEmpty(message = "Please enter your last name")
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname The user's surname (family name)
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return The user's given name (first name)
     */
    @NotEmpty(message = "Please enter your first name")
    public String getGivenName() {
        return givenName;
    }

    /**
     * @param givenName The user's given name (first name)
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * @return The company with which this user is associated.
     * @remarks If specified, 
     * the company name will be displayed to others in all cases, in place of the user's
     * first and last names.
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company The company with which this user is associated.
     * @remarks If specified, 
     * the company name will be displayed to others in all cases, in place of the user's
     * first and last names.
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return A short biographic message (not used).
     */
    public String getBio() {
        return bio;
    }

    /**
     * @param bio A short biographic message (not used).
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * @return The cryptographic hash of the user's password (should be null in most cases)
     */
    public ShaHash getPassword() {
        return password;
    }

    /**
     * @param password The cryptographic hash of the user's password (should be null in most cases)
     */
    public void setPassword(ShaHash password) {
        this.password = password;
    }
}
