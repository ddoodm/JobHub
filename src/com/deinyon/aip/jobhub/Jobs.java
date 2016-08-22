package com.deinyon.aip.jobhub;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Jobs
{
    private LinkedHashMap<UUID, Job> jobList = new LinkedHashMap<>();

    public Collection<Job> getAllJobs() {
        return jobList.values();
    }

    public void saveJob(Job job) {
        job.setId(UUID.randomUUID());
        jobList.put(job.getId(), job);
    }

    public Job loadJob(int index) {
        return jobList.get(index);
    }

    public Jobs ()
    {
        // Set up defaults
        saveJob(new Job(JobDescription.CreateJobDescription("Make Toast", "Make a toast for me in Android", LocalDateTime.now().plusDays(15))));
        saveJob(new Job(JobDescription.CreateJobDescription("Do this thing here", "Do a thing and I'll pay you", LocalDateTime.now().plusDays(15))));
    }
}
