package com.deinyon.aip.jobhub.util;

/**
 * Used early in development to generate sequential ID numbers for database
 * records. Universally Unique Identifiers were elected in place of sequential
 * numbering for security and simplicity.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class IdGenerator
{
    private static int currentId = 0;

    public static synchronized int next()
    {
        return currentId++;
    }
}
