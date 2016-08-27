package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.database.JobsDatabaseInMemory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

@ManagedBean(name = "createJobController")
@RequestScoped
public class CreateJobController implements Serializable
{
    private Job job = new Job();

    public Job getJob()
    {
        return job;
    }

    public String saveJob()
    {
        job.prepare();
        JobsDatabaseInMemory.create(job);
        return "jobs?faces-redirect=true";
    }
}
