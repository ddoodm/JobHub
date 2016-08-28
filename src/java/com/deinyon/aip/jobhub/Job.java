package com.deinyon.aip.jobhub;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Job implements Serializable
{
    private UUID id;
    private JobDescription description;
    private JobStatus status;

    public Job() {
        description = new JobDescription();
        status = JobStatus.PROPOSED;
    }

    /**
     * Called when the job's details have been written,
     * and before the job is inserted into a database.
     * Write additional properties to the job.
     */
    public void prepare()
    {
        // Set the job's listing date to now
        description.setListingDate(new Date());
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
    
    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }
}
