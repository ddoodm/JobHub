package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.database.JobDAO;
import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import java.io.IOException;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

/**
 * The Controller Bean which is responsible for most manipulation and
 * presentation of singular Jobs. It is the primary controller of the Job view,
 * and is used to interface with Business Logic and the database.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@SessionScoped
@ManagedBean(name = "jobController")
public class JobController extends GenericController
{
    /**
     * The presently active Job instance
     */
    private Job job;
    
    /**
     * @return The presently active Job instance
     */
    public Job getJob()
    {
        return this.job;
    }
    
    /**
     * Sets the attached file associated with a job (description).
     * This attachment is optionally supplied when the job is created.
     * @remarks This feature is not entirely implemented
     * @param attachment The HTTP form part which is the file stream
     */
    public void setAttachment(Part attachment)
    {
        if(attachment == null)
            return;
        
        String filename = attachment.getSubmittedFileName();
    }
    
    /**
     * @return The file associated with this Job
     * @remarks This feature is not implemented
     */
    public Part getAttachment()
    {
        return null;
    }
    
    /**
     * Creates and initializes a new Job, including configuration of the creation
     * date, and IDs.
     */
    public void prepareNewJob()
    {
        this.job = new Job();
        job.prepare();
    }
    
    /**
     * Loads the Job with the given ID from the database, as the controller's
     * currently active job.
     * @param jobId The ID of the job to load from the database
     * @throws IOException If the job could not be found, or if a database
     * error prevents the query from executing.
     */
    private void loadJob(UUID jobId) throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            this.job = dao.find(jobId);
        }
    }
    
    /**
     * Loads the Job with the given ID from the database, into the currently
     * active job. 
     * @param jobIdString The ID of the job to load, represented as a String.
     * @throws IOException If the job could not be found, or if a database
     * error prevents the query from executing.
     */
    public void loadJob(String jobIdString) throws IOException
    {
        try
        {
            UUID jobUuid = UUID.fromString(jobIdString);
            loadJob(jobUuid);
        }
        catch (IllegalArgumentException e)
        {
            // Handles cases where the Job ID string is null or invalid
            this.job = null;
        }
    }
    
    /**
     * Inserts the presently active Job into the database, as a new entity.
     * @remarks The Job must first have been 'prepared' (ie, it must have an ID).
     * @return The outcome to which the user should be directed.
     * @throws IOException If a database error prevented the Job from being saved.
     * @throws IllegalAccessException If the presently logged-in user is not
     * granted permission to save a new Job listing.
     */
    public String saveJob() throws IOException, IllegalAccessException
    {
        // Here, we associate this job with the presently logged-in user
        User user = new UserController().getActingUser();
        
        // If the user is not an employer, they are not authorized to create a job
        if(!(user instanceof Employer))
            throw new IllegalAccessException("A non-employer attempted to save a Job Listing");
        
        // Associate the job
        job.setEmployer((Employer)user);
        
        try(JobDAO dao = new JobDAO())
        {
            dao.save(job);
        }
        return "jobs";
    }
    
    /**
     * Saves the state of the presently active job into the database.
     * @return The outcome to which the server should redirect.
     * @throws IOException If a database error prevents the job from being
     * updated, or if the job does not already exist on the database.
     */
    public String updateJob() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            dao.update(job);
        }
        return "jobs";
    }
    
    /**
     * Removes the presently active Job from the database.
     * @remarks The Job can be non-destructively removed by calling closeJob().
     * @return The outcome to which the server should redirect.
     * @throws IOException If a database error prevents the job from being
     * deleted, or if the job does not already exist on the database.
     */
    public String deleteJob() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            dao.delete(job);
        }
        return "jobs";
    }
    
    /**
     * If the presently active User is an Employee, the user will be associated
     * with the presently active Job as the Job's Employee.
     * @throws IOException If a database error prevented the Job from being
     * updated.
     * @throws java.lang.IllegalAccessException If the active User is not an
     * Employee.
     */
    public void takeJob() throws IOException, IllegalAccessException
    {
        // Here, we take the current user, and assign them to the job
        User currentUser = new UserController().getActingUser();
        
        // If the user is not an Employee, they cannot take the job
        if(!(currentUser instanceof Employee))
            throw new IllegalAccessException("A non-Employee attempted to take a Job.");
        
        job.delegateTo((Employee)currentUser);
        updateJob();
    }
    
    /**
     * Non-destructively removes the Job from the Job Listing on the 'Jobs' page.
     * @return The outcome to which the User should be redirected.
     * @throws IOException If a database error prevented the Job from being
     * updated.
     */
    public String closeJob() throws IOException
    {
        // We do not delete the job from the database; we close it
        job.close();
        updateJob();
        
        return "jobs";
    }
    
    /**
     * @return True if this job listing could possibly be approved by the
     * current user. False otherwise.
     * @throws java.io.IOException If a database error prevented access to
     * details about the active user.
     */
    public boolean isApprovable() throws IOException
    {
        User actingUser = getUserController().getActingUser();
        return job.isApprovableBy(actingUser);
    }
    
    /**
     * @return True if the active Job's state permits the job to take Payload
     * submissions, and the active user is permitted to post Payloads on this
     * Job. False otherwise.
     * @throws IOException If a database error prevented access to
     * details about the active user.
     */
    public boolean isUserCanPostPayloads() throws IOException
    {
        User actingUser = getUserController().getActingUser();
        return job.canUserPostPayloads(actingUser);
    }
    
    /**
     * @return True if the active Job is in a state which permits sharing of
     * Payloads, and the active User is permitted to view Payloads on this Job.
     * False otherwise.
     * @throws IOException If a database error prevented access to
     * details about the active user.
     */
    public boolean isUserCanSeePayloads() throws IOException
    {
        User actingUser = getUserController().getActingUser();
        return job.canUserSeePayloads(actingUser);
    }
    
    /**
     * @return True if the active Job is being accessed by an Employee who is
     * not the Employee associated with the Job, and the Job is no longer in
     * its 'Proposed' state. False otherwise.
     * @throws IOException If a database error prevented access to
     * details about the active user.
     */
    public boolean isJobTakenByOtherEmployee() throws IOException
    {
        User actingUser = getUserController().getActingUser();
        return job.isJobTakenByOtherEmployee(actingUser);
    }
    
    /**
     * @return True if the Job is in a state whereby closing it is permitted,
     * and if the user is permitted to close the Job. False otherwise.
     * @throws IOException If a database error prevented access to
     * details about the active user.
     */
    public boolean isUserPermittedToCloseJob() throws IOException
    {
        User actingUser = getUserController().getActingUser();
        return job.isUserPermittedToCloseJob(actingUser);
    }
}
