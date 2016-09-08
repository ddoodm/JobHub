package com.deinyon.aip.jobhub;

import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.validator.constraints.*;

public class Job implements Serializable
{
    private UUID id;
    private JobDescription description;
    private Employee employee;
    private Employer employer;
    private JobStatus status;

    public Job() {
        description = new JobDescription();
        status = JobStatus.Proposed;
    }
    
    public Job(UUID id, Employer employer, JobDescription description, JobStatus status)
    {
        this.id = id;
        this.employer = employer;
        this.description = description;
        this.status = status;
    }

    /**
     * Called when the job's details have been written,
     * and before the job is inserted into a database.
     * Write additional properties to the job.
     */
    public void prepare()
    {
        description.prepare();
        
        // Generate a new ID
        id = UUID.randomUUID();
    }

    public Job(JobDescription description)
    {
        this.description = description;
    }

    @NotEmpty(message = "Please enter a short description")
    @Length(min = 3, max = 10000, message = "Please enter a meaningful description below 10,000 characters")
    public JobDescription getDescription() {
        return description;
    }

    public void setDescription(JobDescription description) {
        this.description = description;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
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
