package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.Configuration;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Defines the base functionality of a Data Access Object
 * @author Deinyon Davies <deinyond@gmail.com>
 * @param <TId> The data type which is used to store keys
 * @param <TResource> The data type of the resource that the DAO mutates
 */
public abstract class ResourceDAO<TId, TResource> implements Closeable
{
    /**
     * The underlying connection to the database
     */
    protected Connection connection;
    
    /**
     * Initializes the DAO with a new database connection used only by this DAO
     * @throws IOException If a connection to the database could not be established
     */
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
    
    /**
     * Initializes the DAO using a pre-initialized database connection
     * @remarks Closing this DAO will close the underlying connection
     * @param connection 
     */
    public ResourceDAO(Connection connection)
    {
        this.connection = connection;
    }
    
    /**
     * Retrieves the resource with the specified ID
     * @param id The ID of the resource to retrieve
     * @return The resource with the specified ID
     * @throws IOException If a database error prevented the query from executing
     */
    public abstract TResource find (TId id) throws IOException;
    
    /**
     * @return All entries in the database of the resource which this database holds
     * @throws IOException IOException If a database error prevented the query from executing
     */
    public abstract Collection<TResource> getAll() throws IOException;
    
    /**
     * Inserts a new resource into the database
     * @param resource The DTO which conveys the data to be inserted into the database
     * @throws IOException IOException If a database error prevented the query from executing
     */
    public abstract void save(TResource resource) throws IOException;
    
    /**
     * Removes a resource from the database
     * @param resource The resource which should be removed from the database
     * @return True if the resource was removed from the database successfully, false otherwise.
     * @throws IOException IOException If a database error prevented the query from executing
     */
    public abstract boolean delete(TResource resource) throws IOException;
    
    /**
     * Updates an existing resource in the database with the values specified by the DTO
     * @param resource The DTO which specifies the new values with which to update the database
     * @throws IOException IOException If a database error prevented the query from executing
     */
    public abstract void update(TResource resource) throws IOException;
    
    /**
     * @return The total number of entries in the database of the type that this DAO mutates
     * @throws IOException IOException If a database error prevented the query from executing
     */
    public abstract int count() throws IOException;
    
    /**
     * Closes the underlying database connection
     * @throws IOException If the database connection could not be closed
     */
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
