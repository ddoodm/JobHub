/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.model;

import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.User;
import java.util.Date;
import java.util.UUID;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class JobPayload
{
    private UUID id, jobId;
    private Employee author;
    private int version;
    private String details;
    private Date submissionDate;
    
    public JobPayload()
    {
        
    }

    public JobPayload(UUID id, UUID jobId, Employee author, int version, String details, Date submissionDate) {
        this.id = id;
        this.jobId = jobId;
        this.author = author;
        this.version = version;
        this.details = details;
        this.submissionDate = submissionDate;
    }
    
    public void prepare()
    {
        // First, we assign the payload a new UUID if it does not already have one
        if(getId() == null)
            setId(UUID.randomUUID());
        
        // Set the submission date to now
        setSubmissionDate(new Date());
    }
    
    public boolean isUserAuthorOfPayload(User user)
    {
        // If the user is not an employee, they could not have been the author
        if(!(user instanceof Employee))
            return false;
        
        return this.author.equals(user);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Length(min = 4, max = 10000, message = "Please write something meaningful, but less than 10,000 characters.")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
}
