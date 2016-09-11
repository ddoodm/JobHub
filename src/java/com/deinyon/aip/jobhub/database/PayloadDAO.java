/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.model.JobDescription;
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

public class PayloadDAO extends ResourceDAO<UUID, JobPayload>
{
    public PayloadDAO() throws IOException
    {
        super();
    }
    
    public PayloadDAO(Connection connection)
    {
        super(connection);
    }
    
    private <T extends User> T loadUser(String username, UserClassification classifier) throws IOException
    {
        // We wish to recycle the current connection, so we do not close the new DAO
        return new UserDAO(connection).findUserOfType(username, classifier);
    }
    
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

    @Override
    public List<JobPayload> getAll() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int count() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private interface StatementSubstitutor
    {
        public void run(PreparedStatement preparedStatement) throws SQLException;
    }
}
