/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.JobDescription;
import com.deinyon.aip.jobhub.JobStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobDAO implements ResourceDAO<Job>
{
    private Connection connection;
    
    public JobDAO(Connection connection)
    {
        this.connection = connection;
    }
    
    /**
     * Builds a Job object from a row of tabular results.
     * Remarks: this overload is used when the Job's ID is already known.
     * @param jobId The known ID of the job
     * @param row An initialized ResultSet which points to a valid row of Job results,
     * inner-joined with JobDetails.
     * @return A Job DTO from the data in the row
     * @throws SQLException 
     */
    private Job buildJobFromRow(UUID jobId, ResultSet row)
            throws SQLException
    {
        // Build the UUID from a UUID string
        UUID descriptionId = UUID.fromString(row.getString("description_id"));

        // Validate state enum value
        int stateId = row.getInt("state");
        if(stateId > JobStatus.values().length)
            throw new IllegalArgumentException("Job 'status' is not a valid JobStatus status.");
        JobStatus jobStatus = JobStatus.values()[stateId];

        // Create the DTO(s)
        JobDescription jobDesc = new JobDescription(
                descriptionId,
                row.getString("title"),
                row.getString("details"),
                row.getBigDecimal("payment"),
                row.getDate("listing_date"),
                row.getDate("end_date")
            );
        return new Job(jobId, jobDesc, jobStatus);
    }
    
    /**
     * Builds a Job object from a row of tabular results.
     * Remarks: this overload is used when the Job's ID is not already known.
     * @param row An initialized ResultSet which points to a valid row of Job results,
     * inner-joined with JobDetails.
     * @return A Job DTO from the data in the row
     * @throws SQLException 
     */
    private Job buildJobFromRow(ResultSet row)
            throws SQLException
    {
        UUID jobId = UUID.fromString(row.getString("job_id"));
        return buildJobFromRow(jobId, row);
    }
    
    @Override
    public Job find(UUID jobId)
        throws SQLException
    {
        // The SQL query selects the Job and Job Description with the given ID
        String query =
                "SELECT Jobs.description_id AS description_id, state, title, details, payment, listing_date, end_date " +
                "FROM Jobs " +
                "INNER JOIN JobDescriptions " +
                "ON Jobs.description_id = JobDescriptions.description_id " +
                "WHERE job_id = ?";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute query parameters
            preparedStatement.setString(1, jobId.toString());

            // Execute the query
            ResultSet results = preparedStatement.executeQuery();

            // One one row is anticipated
            if(!results.next())
                return null;
            
            // Build Job DTOs
            return buildJobFromRow(jobId, results);
        }
    }

    @Override
    public List<Job> getAll()
            throws SQLException
    {
        String query =
                "SELECT job_id, Jobs.description_id AS description_id, state, title, details, payment, listing_date, end_date " +
                "FROM Jobs " +
                "INNER JOIN JobDescriptions " +
                "ON Jobs.description_id = JobDescriptions.description_id ";
        
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query))
        {
            List<Job> jobList = new ArrayList<>();
            
            while(results.next())
                jobList.add(buildJobFromRow(results));
            
            return jobList;
        }
    }
    
}
