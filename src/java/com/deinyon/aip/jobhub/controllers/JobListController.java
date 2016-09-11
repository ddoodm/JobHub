package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.database.JobDAO;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.Collection;

/**
 * The Controller Bean which interfaces the "Job List" Facelet with the database.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@ManagedBean(name = "jobListController")
@RequestScoped
public class JobListController implements Serializable
{
    /**
     * @return A collection of all visible Jobs.
     * @throws IOException If a database error prevented the query from executing.
     */
    public Collection<Job> getAllJobs() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            return dao.getAll();
        }
    }

    /**
     * @return True if there are presently Jobs available to display.
     * @throws IOException If a database error prevented the query from executing.
     */
    public boolean isJobListPopulated() throws IOException
    {
        try(JobDAO dao = new JobDAO())
        {
            return dao.count() > 0;
        }
    }
}
