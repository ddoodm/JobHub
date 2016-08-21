package com.deinyon.aip.jobhub;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.UUID;

@ManagedBean(name = "job")
@SessionScoped
public class Job implements Serializable
{
    private UUID ID;
    private JobDescription description;

    public Job() { }

    public Job(JobDescription description)
    {
        this.description = description;
        ID = UUID.randomUUID();
    }

    public JobDescription getDescription() {
        return description;
    }

    public void setDescription(JobDescription description) {
        this.description = description;
    }

    public String getId() {
        return ID.toString();
    }
}
