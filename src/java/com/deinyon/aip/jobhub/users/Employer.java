package com.deinyon.aip.jobhub.users;

import com.deinyon.aip.jobhub.database.UserClassification;
import com.deinyon.aip.jobhub.util.ShaHash;

/**
 * A User of JobHub who can create and manage Jobs
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class Employer extends User
{
    /**
     * Creates a new Employer
     */
    public Employer()
    { }

    /**
     * Creates a new Employer with known parameters
     * @param username The user-name (identifier) of this user
     * @param password The hash of the user's password (should be null in most cases)
     * @param email The user's E-Mail address
     * @param surname The user's surname (family name)
     * @param givenName The user's given name (first name)
     * @param company The company with which this user is associated. If specified, 
     * the company name will be displayed to others in all cases, in place of the user's
     * first and last names.
     */
    public Employer(String username, ShaHash password, String email, String surname, String givenName, String company)
    {
        super(username, password, email, surname, givenName, company);
    }
    
    /**
     * @return The classification identifier which denotes the type of this user.
     */
    @Override
    public UserClassification getClassifier()
    {
        return UserClassification.Employer;
    }
}
