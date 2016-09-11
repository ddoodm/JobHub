package com.deinyon.aip.jobhub.database;

/**
 * Object-relational mapping utility which is used to differentiate users types
 * in the database, which is determined by inheritance in business logic.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public enum UserClassification
{
    Employee, Employer, Unknown
}
