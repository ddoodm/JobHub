/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.database.JobDAO;
import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@RequestScoped
@ManagedBean(name = "jobController")
public class JobController implements Serializable
{
    private Job job;
    private UserController userController;
    
    public Job getJob()
    {
        return this.job;
    }
    
    private UserController getUserController()
    {
        if(this.userController == null)
            return this.userController = new UserController();
        return this.userController;
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
    
    private void loadJob(UUID jobId) throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            this.job = dao.find(jobId);
        }
    }
    
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
    
    public String updateJob() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            dao.update(job);
        }
        return "jobs";
    }
    
    public String deleteJob() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            dao.delete(job);
        }
        return "jobs";
    }
    
    /**
     * @return True if this job listing could possibly be approved by the current user. False otherwise.
     * @throws java.io.IOException
     */
    public boolean isApprovable() throws IOException
    {
        User actingUser = getUserController().getActingUser();
        return job.isApprovableBy(actingUser);
    }
    
    public boolean isUserCanPostPayloads() throws IOException
    {
        return job.canUserPostPayloads(getUserController().getActingUser());
    }
    
    public void takeJob() throws IOException
    {
        // Here, we take the current user, and assign them to the job
        Employee currentUser = new UserController().<Employee>getActingUserTyped();
        job.delegateTo(currentUser);
        updateJob();
    }
}
