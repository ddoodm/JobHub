package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.Jobs;
import com.deinyon.aip.jobhub.database.JobDAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ManagedBean(name = "jobListController")
@RequestScoped
public class JobListController implements Serializable
{
    private DataSource dataSource;
    private Jobs jobs;
    
    public JobListController() throws NamingException
    {
        jobs = new Jobs();
        dataSource = (DataSource)InitialContext.doLookup("jdbc/jobhub");
    }

    public Collection<Job> getAllJobs() throws NamingException, SQLException
    {
        try(Connection conn = dataSource.getConnection())
        {
            JobDAO jobDao = new JobDAO(conn);
            return jobDao.getAll();
        }
    }

    public boolean isJobsExist() throws NamingException, SQLException
    {
        return getAllJobs().size() > 0;
    }
}
