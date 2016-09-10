/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.model.JobDescription;
import com.deinyon.aip.jobhub.model.JobPayload;
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
    
    private JobPayload buildPayloadFromRwo(ResultSet row) throws SQLException
    {
        UUID payloadId = UUID.fromString(row.getString("payload_id"));
        UUID jobId = UUID.fromString(row.getString("job_id"));
        
        return new JobPayload(
                payloadId,
                jobId,
                row.getInt("version"),
                row.getString("details"),
                row.getDate("submission_date")
        );
    }
    
    public Collection<JobPayload> getForJob(UUID jobId) throws IOException
    {
        String query = 
                "SELECT payload_id, job_id, version, details, submission_date, attachment_id " +
                "FROM JobPayloads " +
                "WHERE job_id=?";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute query parameters
            preparedStatement.setString(1, jobId.toString());

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
    
    @Override
    public void save(JobPayload payload) throws IOException
    {
        // First, we assign the payload a new UUID if it does not already have one
        if(payload.getId() == null)
            payload.setId(UUID.randomUUID());
        
        // The query inserts a new Payload record
        String query = 
                "INSERT INTO JobPayloads (payload_id, job_id, version, details, submission_date) " +
                "VALUES (?,?,?,?,?)";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute parameters
            preparedStatement.setString(1, payload.getId().toString());
            preparedStatement.setString(2, payload.getJobId().toString());
            preparedStatement.setInt(3, payload.getVersion());
            preparedStatement.setString(4, payload.getDetails());
            preparedStatement.setDate(5, SqlDateConverter.toSqlDate(payload.getSubmissionDate()));
            
            // Execute the query
            preparedStatement.executeUpdate();
        }
        catch(Exception ex)
        {
            throw new IOException("Could not save the specified resource", ex);
        }
    }
    
    @Override
    public JobPayload find(UUID id) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<JobPayload> getAll() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(JobPayload resource) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(JobPayload resource) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int count() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
