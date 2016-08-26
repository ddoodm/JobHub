package com.deinyon.aip.jobhub;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Job implements Serializable
{
    private UUID id;
    private JobDescription description;

    public Job() {
        description = new JobDescription();
    }

    /**
     * Called when the job's details have been written,
     * and before the job is inserted into a database.
     * Write additional properties to the job.
     */
    public void prepare()
    {
        // Set the job's listing date to now
        description.setListingDate(LocalDateTime.now());
    }

    public Job(JobDescription description)
    {
        this.description = description;
    }

    public JobDescription getDescription() {
        return description;
    }

    public void setDescription(JobDescription description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }
}
