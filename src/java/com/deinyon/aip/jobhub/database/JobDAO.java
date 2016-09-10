/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.Configuration;
import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.JobDescription;
import com.deinyon.aip.jobhub.JobStatus;
import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import com.deinyon.aip.jobhub.util.SqlDateConverter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JobDAO implements ResourceDAO<UUID, Job>
{
    private Connection connection;
    
    public JobDAO() throws IOException
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
    
    private <T extends User> T loadUser(String username, UserClassification classifier) throws IOException
    {
        // We wish to recycle the current connection, so we do not close the new DAO
        UserDAO userDao = new UserDAO(connection);
        return userDao.findUserOfType(username, classifier);
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
            throws SQLException, IOException
    {
        // Build the UUID from a UUID string
        UUID descriptionId = UUID.fromString(row.getString("description_id"));

        // Validate state enum value
        JobStatus jobStatus = JobStatus.valueOf(row.getString("state"));
        
        // Load the Employer and Employee
        Employer employer = loadUser(row.getString("employer_id"), UserClassification.Employer);

        // Create the DTO(s)
        JobDescription jobDesc = new JobDescription(
                descriptionId,
                row.getString("title"),
                row.getString("details"),
                row.getBigDecimal("payment"),
                row.getDate("listing_date"),
                row.getDate("end_date")
            );
        Job job = new Job(jobId, employer, jobDesc, jobStatus);
        
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
     * @throws SQLException 
     */
    private Job buildJobFromRow(ResultSet row)
            throws SQLException, IOException
    {
        UUID jobId = UUID.fromString(row.getString("job_id"));
        return buildJobFromRow(jobId, row);
    }
    
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
    
    private void updateJobDescription(JobDescription desc)
            throws SQLException
    {
        String query =
                "UPDATE JobDescriptions " +
                "SET description_id=?, title=?, details=?, listing_date=?, end_date=?, payment=?" +
                "WHERE description_id=?";
       
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(7, desc.getId().toString());
            updateJobDescription(desc, preparedStatement);
        }
    }
    
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

    @Override
    public List<Job> getAll() throws IOException
    {
        String query =
                "SELECT job_id, Jobs.description_id AS description_id, employer_id, employee_id, state, title, details, payment, listing_date, end_date " +
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
        catch(Exception ex)
        {
            throw new IOException("Could not retrieve the resource", ex);
        }
    }
    
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
    
    @Override
    public int count() throws IOException
    {
        String query = "SELECT count(1) FROM Jobs";
        
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query))
        {
            if(!results.next())
                return 0;
            return results.getInt(1);
        }
        catch(Exception ex)
        {
            throw new IOException("Could not query the specified resource", ex);
        }
    }

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
