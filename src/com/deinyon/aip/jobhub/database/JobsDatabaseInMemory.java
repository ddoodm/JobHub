package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.Job;

import java.util.Collection;
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

    /**
     * Seeds the in-memory database with sample data
     */
    public void seedDatabase()
    {/*
        create(new Job(JobDescription.CreateJobDescription("Make Toast", "Make a toast for me in Android", LocalDateTime.now().plusDays(15))));
        create(new Job(JobDescription.CreateJobDescription("Do this thing here", "Do a thing and I'll pay you", LocalDateTime.now().plusDays(15))));
        create(new Job(JobDescription.CreateJobDescription("Do another thingo!", "Do a thing and I'll pay you", LocalDateTime.now().plusDays(15))));
    */}
}
