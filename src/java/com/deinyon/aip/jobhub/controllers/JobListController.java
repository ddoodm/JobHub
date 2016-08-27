package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.Jobs;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.Collection;

@ManagedBean(name = "jobListController")
@RequestScoped
public class JobListController implements Serializable
{
    private Jobs jobs = new Jobs();

    public Collection<Job> getAllJobs() {
        return jobs.getAllJobs();
    }

    public boolean isJobsExist() {
        return getAllJobs().size() > 0;
    }
}
