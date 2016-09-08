/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.users;

import com.deinyon.aip.jobhub.database.UserClassification;
import com.deinyon.aip.jobhub.util.ShaHash;

/**
 *
 * @author Ddoodm
 */
public class Employee extends User
{
    public Employee()
    { }

    public Employee(String username, ShaHash password, String email, String surname, String givenName, String company)
    {
        super(username, password, email, surname, givenName, company);
    }

    @Override
    public UserClassification getClassifier()
    {
        return UserClassification.Employee;
    }
}
