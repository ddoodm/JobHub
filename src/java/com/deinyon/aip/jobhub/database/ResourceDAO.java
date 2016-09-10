/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.Configuration;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class ResourceDAO<TId, TResource> implements Closeable
{
    protected Connection connection;
    
    public ResourceDAO() throws IOException
    {
        try
        {
            DataSource dataSource = (DataSource)InitialContext.doLookup(Configuration.DATABASE_RESOURCE_NAME);
            this.connection = dataSource.getConnection();
        }
        catch(SQLException | NamingException ex)
        {
            throw new IOException("A database error prevented the DAO from initializing.", ex);
        }
    }
    
    public ResourceDAO(Connection connection)
    {
        this.connection = connection;
    }
    
    public abstract TResource find (TId id) throws IOException;
    public abstract List<TResource> getAll() throws IOException;
    public abstract void save(TResource resource) throws IOException;
    public abstract boolean delete(TResource resource) throws IOException;
    public abstract void update(TResource resource) throws IOException;
    public abstract int count() throws IOException;
    
    @Override
    public void close() throws IOException
    {
        try
        {
            connection.close();
        }
        catch(SQLException ex)
        {
            throw new IOException(ex);
        }
    }
}
