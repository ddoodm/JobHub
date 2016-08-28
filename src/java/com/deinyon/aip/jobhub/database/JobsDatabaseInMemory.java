package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.Job;
import com.deinyon.aip.jobhub.JobDescription;
import java.math.BigDecimal;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.UUID;

public class JobsDatabaseInMemory
{
    /**
     * An in-memory database (hash map) of Jobs, which is retained across all connections
     */
    private static LinkedHashMap<UUID, Job> jobMap = new LinkedHashMap<>();

    public JobsDatabaseInMemory()
    {
        seedDatabase();
    }

    public static synchronized Collection<Job> loadAllJobs()
    {
        return jobMap.values();
    }

    public static synchronized void create(Job job)
    {
        job.setId(UUID.randomUUID());
        jobMap.put(job.getId(), job);
    }

    public static synchronized Job read(UUID id)
    {
        return jobMap.get(id);
    }
    
    public static synchronized void update(Job job)
    {
        jobMap.remove(job.getId());
        jobMap.put(job.getId(), job);
    }

    /**
     * Seeds the in-memory database with sample data
     */
    public static void seedDatabase()
    {
        create(new Job(JobDescription.CreateJobDescription("Make Toast", "Make a toast for me in Android", new BigDecimal(425), new Date(119, 4, 12))));
        create(new Job(JobDescription.CreateJobDescription("Do this thing here", "Do a thing and I'll pay you", new BigDecimal(1021), new Date(118, 9, 4))));
        create(new Job(JobDescription.CreateJobDescription("Do another thingo!", "Do a thing and I'll pay you", new BigDecimal(4200),  new Date(120, 5, 3))));
    }
}
