package com.deinyon.aip.jobhub.model;

import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.User;
import java.util.Date;
import java.util.UUID;
import org.hibernate.validator.constraints.Length;

/**
 * The Business Object and Data Transfer Object which represents a Payload for a Job
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class JobPayload
{
    /**
     * The identifier of this payload
     */
    private UUID id;
    
    /**
     * The identifier of the job with which this payload is associated
     */
    private UUID jobId;
    
    /**
     * The Employee who authored this payload
     */
    private Employee author;
    
    /**
     * The version number of this payload (not used)
     */
    private int version;
    
    /**
     * The details of this payload (the payload contents)
     */
    private String details;
    
    /**
     * The date that this payload was created
     */
    private Date submissionDate;
    
    /**
     * Creates a new Payload
     */
    public JobPayload()
    {
        
    }

    /**
     * Creates a Payload with known properties
     * @param id The identifier of this payload
     * @param jobId The identifier of the job with which this payload is associated
     * @param author The Employee who authored this payload
     * @param version The version number of this payload (not used)
     * @param details The details of this payload (the payload contents)
     * @param submissionDate The date that this payload was created
     */
    public JobPayload(UUID id, UUID jobId, Employee author, int version, String details, Date submissionDate) {
        this.id = id;
        this.jobId = jobId;
        this.author = author;
        this.version = version;
        this.details = details;
        this.submissionDate = submissionDate;
    }
    
    /**
     * Called when the Payload's details have been written,
     * and before the Payload is inserted into a database.
     * Writes additional properties to the Payload, including its ID.
     */
    public void prepare()
    {
        // First, we assign the payload a new UUID if it does not already have one
        if(getId() == null)
            setId(UUID.randomUUID());
        
        // Set the submission date to now
        setSubmissionDate(new Date());
    }
    
    /**
     * @param user A user with which to check.
     * @return True if the specified user is the author of this payload, false otherwise.
     */
    public boolean isUserAuthorOfPayload(User user)
    {
        // If the user is not an employee, they could not have been the author
        if(!(user instanceof Employee))
            return false;
        
        return this.author.equals(user);
    }

    /**
     * @return The identifier of this payload
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id The identifier of this payload
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return The identifier of the job with which this payload is associated
     */
    public UUID getJobId() {
        return jobId;
    }

    /**
     * @param jobId The identifier of the job with which this payload is associated
     */
    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    /**
     * @return Employee who authored this payload
     */
    public Employee getAuthor() {
        return author;
    }

    /**
     * @param author Employee who authored this payload
     */
    public void setAuthor(Employee author) {
        this.author = author;
    }

    /**
     * @return The version number of this payload (not used)
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version The version number of this payload (not used)
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return The details of this payload (the payload contents)
     */
    @Length(min = 4, max = 10000, message = "Please write something meaningful, but less than 10,000 characters.")
    public String getDetails() {
        return details;
    }

    /**
     * @param details The details of this payload (the payload contents)
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return The date that this payload was created
     */
    public Date getSubmissionDate() {
        return submissionDate;
    }

    /**
     * @param submissionDate The date that this payload was created
     */
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
}
