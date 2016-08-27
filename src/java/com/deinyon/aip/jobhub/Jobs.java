package com.deinyon.aip.jobhub;

import com.deinyon.aip.jobhub.database.JobsDatabaseInMemory;

import java.util.Collection;
import java.util.UUID;

public class Jobs
{
    private static final JobsDatabaseInMemory database = new JobsDatabaseInMemory();

    public Collection<Job> getAllJobs() {
        return database.loadAllJobs();
    }

    public void saveJob(Job job) {
        database.create(job);
    }

    public Job loadJob(UUID id) {
        return database.read(id);
    }
}
