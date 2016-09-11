/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.model.*;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import com.deinyon.aip.jobhub.util.SqlDateConverter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * The Data Access Object which accesses and mutates Job DTOs with the database.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class JobDAO extends ResourceDAO<UUID, Job>
{   
    /**
     * Initializes the DAO using a new database context.
     * @remarks This DAO opens un-managed network connections, and should be
     * instantiated within a try-with-resources block, or should be closed
     * manually after use by calling close().
     * @throws IOException If a database connection could not be established
     */
    public JobDAO() throws IOException
    {
        super();
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
     * Utility function which uses a Payload DAO to retrieve the Payloads associated
     * with the job which is identified by the specified ID.
     * @param jobId The ID of the job whose payloads we wish to retrieve
     * @return A collection of payloads associated with the specified job
     * @throws IOException If a database error prevented the payloads from being retrieved.
     */
    private Collection<JobPayload> loadPayloads(UUID jobId) throws IOException
    {
        return new PayloadDAO(connection).getForJob(jobId);
    }
    
    /**
     * Builds a Job object from a row of tabular results.
     * Remarks: this overload is used when the Job's ID is already known.
     * @param jobId The known ID of the job
     * @param row An initialized ResultSet which points to a valid row of Job results,
     * inner-joined with JobDetails.
     * @return A Job DTO from the data in the row
     * @throws SQLException If a database error prevented the DTO from being constructed.
     */
    private Job buildJobFromRow(UUID jobId, ResultSet row)
            throws SQLException, IOException
    {
        // Build the UUID from a UUID string
        UUID descriptionId = UUID.fromString(row.getString("description_id"));

        // Validate state enum value
        JobStatus jobStatus = JobStatus.valueOf(row.getString("state"));
        
        // Load the Employer and Employee
        Employer employer = loadUser(row.getString("employer_id"), UserClassification.Employer);
        
        // Load this job's payloads
        Collection<JobPayload> payloads = loadPayloads(jobId);

        // Create the DTO(s)
        JobDescription jobDesc = new JobDescription(
                descriptionId,
                row.getString("title"),
                row.getString("details"),
                row.getBigDecimal("payment"),
                row.getDate("listing_date"),
                row.getDate("end_date")
            );
        Job job = new Job(jobId, employer, jobDesc, jobStatus, payloads);
        
        // Load relation IDs
        job.setEmployee(loadUser(row.getString("employee_id"), UserClassification.Employee));
        
        return job;
    }
    
    /**
     * Builds a Job object from a row of tabular results.
     * Remarks: this overload is used when the Job's ID is not already known.
     * @param row An initialized ResultSet which points to a valid row of Job results,
     * inner-joined with JobDetails.
     * @return A Job DTO from the data in the row
     * @throws SQLException If a database error prevented the DTO from being constructed.
     */
    private Job buildJobFromRow(ResultSet row)
            throws SQLException, IOException
    {
        UUID jobId = UUID.fromString(row.getString("job_id"));
        return buildJobFromRow(jobId, row);
    }
    
    /**
     * Utility which substitutes parameters into a prepared statement, and executes it. 
     * @param desc The Job Description DTO which is being conveyed to the database.
     * @param preparedStatement The prepared statement which specifies the query.
     * @throws SQLException If an error prevented the query from executing.
     */
    private void updateJobDescription(JobDescription desc, PreparedStatement preparedStatement) throws SQLException
    {
        // Substitute query parameters
        preparedStatement.setString(1, desc.getId().toString());
        preparedStatement.setString(2, desc.getTitle());
        preparedStatement.setString(3, desc.getDetails());
        preparedStatement.setDate(4, SqlDateConverter.toSqlDate(desc.getListingDate()));
        preparedStatement.setDate(5, SqlDateConverter.toSqlDate(desc.getTargetEndDate()));
        preparedStatement.setBigDecimal(6, desc.getPayment());

        // Execute the query
        preparedStatement.executeUpdate();
    }
    
    /**
     * Inserts a new Job Description DTO into the database
     * @param desc The Job Description DTO to insert into the database
     * @throws SQLException If an error prevented the insertion from completing
     */
    private void saveJobDescription(JobDescription desc)
            throws SQLException
    {
        String query =
                "INSERT INTO JobDescriptions (description_id, title, details, listing_date, end_date, payment) " +
                "VALUES (?,?,?,?,?,?)";
       
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            updateJobDescription(desc, preparedStatement);
        }
    }
    
    /**
     * Updates an existing Job Description DTO in the database
     * @param desc The Job Description DTO which describes the changes to make
     * to the database.
     * @throws SQLException If the update operation failed.
     */
    private void updateJobDescription(JobDescription desc)
            throws SQLException
    {
        String query =
                "UPDATE JobDescriptions " +
                "SET description_id=?, title=?, details=?, listing_date=?, end_date=?, payment=?" +
                "WHERE description_id=?";
       
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Set the last parameter, which denotes the record to update
            preparedStatement.setString(7, desc.getId().toString());
            updateJobDescription(desc, preparedStatement);
        }
    }
    
    /**
     * Utility which substitutes Job parameters into a prepared statement, and executes it. 
     * @param job The Job DTO which is being conveyed to the database.
     * @param preparedStatement The prepared statement which specifies the query.
     * @throws SQLException If an error prevented the query from executing.
     */
    private void updateJob(Job job, PreparedStatement preparedStatement) throws SQLException
    {
        // Substitute query parameters
        preparedStatement.setString(1, job.getId().toString());
        preparedStatement.setString(2, job.getEmployer().getUsername());
        preparedStatement.setString(3, job.getDescription().getId().toString());
        preparedStatement.setString(4, job.getStatus().name());
        
        // Set optional 'employee' value
        if(job.getEmployee() != null)
            preparedStatement.setString(5, job.getEmployee().getUsername());
        else
            preparedStatement.setNull(5, Types.VARCHAR);

        // Execute the query
        preparedStatement.executeUpdate();
    }
    
    /**
     * Inserts a new Job DTO into the database
     * @param job The Job DTO to insert into the database
     * @throws SQLException If an error prevented the insertion from completing
     */
    private void saveJob(Job job) throws SQLException
    {
        String query =
                "INSERT INTO Jobs (job_id, employer_id, description_id, state, employee_id) " +
                "VALUES (?,?,?,?,?)";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            updateJob(job, preparedStatement);
        }
    }
    
    /**
     * Updates an existing Job DTO in the database
     * @param job The Job DTO which describes the changes to make
     * to the database.
     * @throws SQLException If the update operation failed.
     */
    private void updateJob(Job job) throws SQLException
    {
        String query =
                "UPDATE Jobs " +
                "SET job_id=?, employer_id=?, description_id=?, state=?, employee_id=? " +
                "WHERE job_id=?";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(6, job.getId().toString());
            updateJob(job, preparedStatement);
        }
    }
    
    /**
     * Locates and retrieves the Job in the database with the specified ID
     * @param jobId The ID of the Job which should be retrieved
     * @return The Job in the database with the specified ID. Null if the
     * record does not exist.
     * @throws IOException If a database error prevented the query from executing
     */
    @Override
    public Job find(UUID jobId) throws IOException
    {
        // The SQL query selects the Job and Job Description with the given ID
        String query =
                "SELECT Jobs.description_id AS description_id, employer_id, employee_id, state, title, details, payment, listing_date, end_date " +
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
        catch(Exception ex)
        {
            throw new IOException("Could not find the specified resource", ex);
        }
    }

    /**
     * @return A collection storing all jobs in the database which are not closed.
     * @throws IOException If a database error prevented the query from executing
     */
    @Override
    public Collection<Job> getAll() throws IOException
    {
        // The query selects all jobs that are not closed, and joins each with its associated Job Description
        String query =
                "SELECT job_id, Jobs.description_id AS description_id, employer_id, employee_id, state, title, details, payment, listing_date, end_date " +
                "FROM Jobs " +
                "INNER JOIN JobDescriptions " +
                "ON Jobs.description_id = JobDescriptions.description_id " +
                "WHERE state <> ?";
        
        try(PreparedStatement statement = connection.prepareStatement(query))
        {
            // Set the name of the 'Closed' state
            statement.setString(1, JobStatus.Closed.name());
            
            try(ResultSet results = statement.executeQuery())
            {
                Collection<Job> jobList = new ArrayList<>();

                while(results.next())
                    jobList.add(buildJobFromRow(results));

                return jobList;
            }
        }
        catch(Exception ex)
        {
            throw new IOException("Could not retrieve the resource", ex);
        }
    }
    
    /**
     * Inserts a new job, and its associated description, into the database
     * @param job The Job DTO to insert into the database
     * @throws IOException If a database error prevented the query from executing
     */
    @Override
    public void save(Job job) throws IOException
    {
        // We save the Job Description first, because the Job depends on it
        try
        {
            saveJobDescription(job.getDescription());
            saveJob(job);
        }
        catch(Exception ex)
        {
            throw new IOException("Could not save the specified resource", ex);
        }
    }
    
    /**
     * Removes the job (and its associated description by its cascade-on-delete
     * constraint).
     * @param job The Job DTO to remove from the database
     * @return True if the record was deleted successfully
     * @throws IOException If a database error prevented the query from executing
     */
    @Override
    public boolean delete(Job job) throws IOException
    {
        String query =
                "DELETE FROM Jobs " +
                "WHERE job_id = ?";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute query parameters
            preparedStatement.setString(1, job.getId().toString());

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
     * Updates an existing job in the database with the values specified in a DTO.
     * @param job The DTO with which the database will be updated
     * @throws IOException If a database error prevented the query from executing
     */
    @Override
    public void update(Job job) throws IOException
    {
        try
        {
            updateJobDescription(job.getDescription());
            updateJob(job);
        }
        catch(Exception ex)
        {
            throw new IOException("Could not update the specified resource", ex);
        }
    }
    
    /**
     * @return The count of all non-closed jobs in the database
     * @throws IOException If a database error prevented the query from executing
     */
    @Override
    public int count() throws IOException
    {
        String query = "SELECT count(1) FROM Jobs WHERE Jobs.state <> ?";
        
        try(PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, JobStatus.Closed.name());
            
            try(ResultSet results = statement.executeQuery())
            {
                if(!results.next())
                    return 0;
                return results.getInt(1);
            }
        }
        catch(Exception ex)
        {
            throw new IOException("Could not query the specified resource", ex);
        }
    }
}
