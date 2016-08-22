package com.deinyon.aip.jobhub.util;

/**
 * Created by Ddoodm on 8/21/2016.
 */
public class IdGenerator
{
    private static int currentId = 0;

    public static synchronized int next()
    {
        return currentId++;
    }
}
