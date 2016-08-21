package com.deinyon.aip.jobhub;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="jobs")
@SessionScoped
public class Jobs implements Serializable
{
    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    private List<Job> jobList = new ArrayList<>();

    public Jobs ()
    {
        // Set up defaults
        jobList.add(new Job(JobDescription.CreateJobDescription("Make Toast", "Make a toast for me in Android", LocalDateTime.now().plusDays(15))));
        jobList.add(new Job(JobDescription.CreateJobDescription("Do this thing here", "Do a thing and I'll pay you", LocalDateTime.now().plusDays(15))));
    }
}
