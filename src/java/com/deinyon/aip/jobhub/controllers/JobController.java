/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.database.JobDAO;
import com.deinyon.aip.jobhub.users.Employer;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

@SessionScoped
@ManagedBean(name = "jobController")
public class JobController implements Serializable
{
    private Job job;
    
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
    
    public void loadJob(String jobIdString) throws IOException
    {
        try
        {
            UUID jobUuid = UUID.fromString(jobIdString);
            
            try(JobDAO dao = new JobDAO())
            {
                this.job = dao.find(jobUuid);
            }
        }
        catch (IllegalArgumentException e)
        {
            // Handles cases where the Job ID string is null or invalid
            this.job = null;
        }
    }
    
    public String updateJob() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            dao.update(job);
        }
        return "jobs?faces-redirect=true";
    }
    
    public String saveJob() throws IOException
    {
        // TODO: Use the real user
        Employer employer = new Employer();
        employer.setUsername("ddoodm");
        job.setEmployer(employer);
        
        try(JobDAO dao = new JobDAO())
        {
            dao.save(job);
        }
        return "jobs?faces-redirect=true";
    }
    
    public String deleteJob() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            dao.delete(job);
        }
        return "jobs?faces-redirect=true";
    }
}
