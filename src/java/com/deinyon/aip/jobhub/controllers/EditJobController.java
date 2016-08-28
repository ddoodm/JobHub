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

@ManagedBean(name = "editJobController")
@SessionScoped
public class EditJobController implements Serializable
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
    
    public String saveJob()
    {
        JobsDatabaseInMemory.update(selectedJob);
        return "jobs";
    }
}
