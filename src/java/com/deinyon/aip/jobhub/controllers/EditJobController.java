/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.database.JobDAO;
import com.deinyon.aip.jobhub.model.Job;
import java.io.IOException;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "editJobController")
public class EditJobController
{
    private Job job;
    
    public Job getJob()
    {
        return this.job;
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
    
    public String updateJob() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            dao.update(job);
        }
        return "jobs";
    }
}
