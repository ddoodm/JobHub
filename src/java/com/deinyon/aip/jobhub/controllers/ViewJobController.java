package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.database.JobsDatabaseInMemory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.UUID;

@ManagedBean(name = "viewJobController")
@RequestScoped
public class ViewJobController
{
    private Job selectedJob;

    public void loadJob(String jobIdString)
    {
        UUID jobUuid = UUID.fromString(jobIdString);
        this.selectedJob = JobsDatabaseInMemory.read(jobUuid);
    }

    public Job getSelectedJob()
    {
        return this.selectedJob;
    }
}
