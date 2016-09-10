package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.database.JobDAO;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.Collection;

@ManagedBean(name = "jobListController")
@RequestScoped
public class JobListController implements Serializable
{
    public Collection<Job> getAllJobs() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            return dao.getAll();
        }
    }

    public boolean isJobsExist() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            return dao.count() > 0;
        }
    }
}
