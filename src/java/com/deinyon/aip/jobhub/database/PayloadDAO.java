package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.model.JobPayload;
import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.User;
import com.deinyon.aip.jobhub.util.SqlDateConverter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * The Data Access Object which accesses and mutates Payload DTOs with the database.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class PayloadDAO extends ResourceDAO<UUID, JobPayload>
{
    /**
     * Initializes the DAO using a new database context.
     * @remarks This DAO opens un-managed network connections, and should be
     * instantiated within a try-with-resources block, or should be closed
     * manually after use by calling close().
     * @throws IOException If a database connection could not be established
     */
    public PayloadDAO() throws IOException
    {
        super();
    }
    
    /**
     * Initializes the DAO using an existing database context
     * @remarks If this DAO is closed, the underlying connection will also be closed.
     * @param connection The database connection that should be used by this DAO
     * to access the database.
     */
    public PayloadDAO(Connection connection)
    {
        super(connection);
    }
    
    /**
     * Utility function which retrieves a User from the User DAO
     * @param <T> The type of user to retrieve (Employee or Employer)
     * @param username The username (identifier) of the user to retrieve
     * @param classifier The class of the type of user to retrieve
     * @return The user with the specified type and username
     * @throws IOException If a database error prevented the user from being retrieved.
     */
    private <T extends User> T loadUser(String username, UserClassification classifier) throws IOException
    {
        // We wish to recycle the current connection, so we do not close the new DAO
        return new UserDAO(connection).findUserOfType(username, classifier);
    }
    
    /**
     * Constructs a Payload DTO object from a database table row
     * @param row A ResultSet which has been initialized to point to a row of Payload
     * @return A Payload DTO which represents the contents of the row
     * @throws SQLException If the query failed to execute
     * @throws IOException If details about the associated user could not be loaded
     */
    private JobPayload buildPayloadFromRwo(ResultSet row) throws SQLException, IOException
    {
        UUID payloadId = UUID.fromString(row.getString("payload_id"));
        UUID jobId = UUID.fromString(row.getString("job_id"));
        String authorId = row.getString("author_id");
        
        // Here, we load the author entirely
        Employee author = loadUser(authorId, UserClassification.Employee);
        
        return new JobPayload(
                payloadId,
                jobId,
                author,
                row.getInt("version"),
                row.getString("details"),
                row.getDate("submission_date")
        );
    }
    
    /**
     * Substitutes parameters into a query using a 'Substitutor' function, and
     * returns A collection of Payloads populated by the specified query.
     * @param query The database query which selects Payloads
     * @param substitutor The substitutor runnable which substitutes parameters
     * into the query.
     * @return The collection of Payloads returned by the query
     * @throws IOException If a database error prevented the query from executing successfully.
     */
    private Collection<JobPayload> substituteAndExecuteQuery(String query, StatementSubstitutor substitutor)
            throws IOException
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute query parameters
            substitutor.run(preparedStatement);

            // Execute the query
            ResultSet results = preparedStatement.executeQuery();

            Collection<JobPayload> payloads = new ArrayList<>();
            
            while(results.next())
                payloads.add(buildPayloadFromRwo(results));
            
            return payloads;
        }
        catch(Exception ex)
        {
            throw new IOException("Could not find the specified resource", ex);
        }
    }
    
    /**
     * Retrieves a collection of Payloads from the database which are associated
     * with the Job with the specified ID
     * @param jobId The ID of the job to which the Payloads should be related
     * @return A collection of payloads which are children of the specified job
     * @throws IOException If a database error prevented the query from executing successfully.
     */
    public Collection<JobPayload> getForJob(UUID jobId) throws IOException
    {
        String query = 
                "SELECT payload_id, job_id, version, details, submission_date, attachment_id, author_id " +
                "FROM JobPayloads " +
                "WHERE job_id=?";
        
        return substituteAndExecuteQuery(query, (PreparedStatement preparedStatement) -> 
        {
            preparedStatement.setString(1, jobId.toString());
        });
    }
    
    /**
     * Inserts a new Job Payload into the database
     * @param payload The Payload DTO to insert into the database
     * @throws IOException If a database error prevented the query from executing successfully.
     */
    @Override
    public void save(JobPayload payload) throws IOException
    {        
        // The query inserts a new Payload record
        String query = 
                "INSERT INTO JobPayloads (payload_id, job_id, version, details, submission_date, author_id) " +
                "VALUES (?,?,?,?,?,?)";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute parameters
            preparedStatement.setString(1, payload.getId().toString());
            preparedStatement.setString(2, payload.getJobId().toString());
            preparedStatement.setInt(3, payload.getVersion());
            preparedStatement.setString(4, payload.getDetails());
            preparedStatement.setDate(5, SqlDateConverter.toSqlDate(payload.getSubmissionDate()));
            preparedStatement.setString(6, payload.getAuthor().getUsername());
            
            // Execute the query
            preparedStatement.executeUpdate();
        }
        catch(Exception ex)
        {
            throw new IOException("Could not save the specified resource", ex);
        }
    }
    
    /**
     * Removes a payload from the database
     * @param payload The payload to delete from the database
     * @return True if the resources was deleted
     * @throws IOException If a database error prevented the query from executing successfully.
     */
    @Override
    public boolean delete(JobPayload payload) throws IOException
    {
        String query =
                "DELETE FROM JobPayloads " +
                "WHERE payload_id = ?";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute query parameters
            preparedStatement.setString(1, payload.getId().toString());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
        catch(Exception ex)
        {
            throw new IOException("Could not delete the specified resource", ex);
        }
    }
    
    /**
     * Retrieves the payload with the specified ID
     * @param id The ID of the payload to retrieve
     * @return The Payload DTO which denotes the payload with the specified ID
     * @throws IOException If a database error prevented the query from executing successfully.
     */
    @Override
    public JobPayload find(UUID id) throws IOException
    {
        String query = 
                "SELECT payload_id, job_id, version, details, submission_date, attachment_id, author_id " +
                "FROM JobPayloads " +
                "WHERE payload_id=?";
        
        Collection<JobPayload> results = substituteAndExecuteQuery(query, (PreparedStatement preparedStatement) -> 
        {
            preparedStatement.setString(1, id.toString());
        });
        
        // Returns the first (and hopefully only) element
        return results.iterator().next();
    }
    
    /**
     * Updates an existing payload in the database with the Version and Details
     * of the specified Payload DTO.
     * @param payload The payload with which the database should be updated
     * @throws IOException If a database error prevented the query from executing successfully.
     */
    @Override
    public void update(JobPayload payload) throws IOException
    {
        String query =
                "UPDATE JobPayloads " +
                "SET version=?, details=? " +
                "WHERE payload_id=?";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, payload.getVersion());
            preparedStatement.setString(2, payload.getDetails());
            preparedStatement.setString(3, payload.getId().toString());
            
            preparedStatement.executeUpdate();
        }
        catch(SQLException ex)
        {
            throw new IOException("Could not update the specified resource.");
        }
    }

    /**
     * Operation not supported for payloads
     */
    @Override
    public List<JobPayload> getAll() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Operation not supported for payloads
     */
    @Override
    public int count() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * A runnable which is used to convey a query parameter substitutor method
     * to a database population function.
     */
    private interface StatementSubstitutor
    {
        public void run(PreparedStatement preparedStatement) throws SQLException;
    }
}
