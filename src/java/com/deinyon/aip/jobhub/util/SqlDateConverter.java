/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.util;

/**
 *
 * @author Ddoodm
 */
public class SqlDateConverter
{
    public static java.sql.Date toSqlDate(java.util.Date utilDate)
    {
        return new java.sql.Date(utilDate.getTime());
    }
}
