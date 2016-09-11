package com.deinyon.aip.jobhub.util;

/**
 * Utility which converts between java.util.Date and java.sql.Date
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class SqlDateConverter
{
    /**
     * Converts a java.sql.Date to a java.util.Date
     * @param utilDate The java.util.Date to convert
     * @return The java.sql.Date representation of the Date
     */
    public static java.sql.Date toSqlDate(java.util.Date utilDate)
    {
        return new java.sql.Date(utilDate.getTime());
    }
}
