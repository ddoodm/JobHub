/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.database.JobsDatabaseInMemory;
import java.io.Serializable;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

@SessionScoped
@ManagedBean(name = "jobController")
public class JobController implements Serializable
{
    private Job job;

    public void loadJob(String jobIdString)
    {
        try
        {
            UUID jobUuid = UUID.fromString(jobIdString);
            this.job = JobsDatabaseInMemory.read(jobUuid);
        }
        catch (IllegalArgumentException e)
        {
            // Handles cases where the Job ID string is null or invalid
            this.job = null;
        }
    }
    
    public Job getJob()
    {
        return this.job;
    }
    
    /**
     * Sets the attached file associated with a job (description).
     * This attachment is optionally supplied when the job is created.
     * @param attachment The HTTP form part which is the file stream
     */
    public void setAttachment(Part attachment)
    {
        if(attachment == null)
            return;
        
        String filename = attachment.getSubmittedFileName();
    }
    
    public Part getAttachment()
    {
        return null;
    }
    
    public void prepareNewJob()
    {
        this.job = new Job();
        job.prepare();
    }
    
    public String updateJob()
    {
        JobsDatabaseInMemory.update(job);
        return "jobs?faces-redirect=true";
    }
    
    public String saveJob()
    {
        JobsDatabaseInMemory.create(job);
        return "jobs?faces-redirect=true";
    }
    
    public String deleteJob()
    {
        JobsDatabaseInMemory.delete(job);
        return "jobs?faces-redirect=true";
    }
}
