package com.deinyon.aip.jobhub;

import java.util.UUID;

public class Job
{
    private UUID id;
    private JobDescription description;

    public Job() { }

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
