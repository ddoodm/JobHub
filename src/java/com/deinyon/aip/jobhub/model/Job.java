package com.deinyon.aip.jobhub.model;

import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.hibernate.validator.constraints.*;

public class Job implements Serializable
{
    private UUID id;
    private JobDescription description;
    private Employee employee;
    private Employer employer;
    private JobStatus status;
    
    private Collection<JobPayload> payloads;

    public Job()
    {
        description = new JobDescription();
        status = JobStatus.Proposed;
        payloads = new ArrayList<>();
    }
    
    public Job(UUID id, Employer employer, JobDescription description, JobStatus status, Collection<JobPayload> payloads)
    {
        this.id = id;
        this.employer = employer;
        this.description = description;
        this.status = status;
        this.payloads = payloads;
    }

    public Job(JobDescription description)
    {
        this.description = description;
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
    
    public void delegateTo(Employee employee)
    {
        this.setEmployee(employee);
        this.status = JobStatus.Approved;
    }
    
    public String getStatusMessage()
    {
        switch(status)
        {
            case Approved:
                return String.format("Approved by %s", employee.getDisplayName());
            default:
                return status.name();
        }
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

    /**
     * @param actingUser The user who is currently logged-in
     * @return True if the job is in a state which permits approval
     */
    public boolean isApprovableBy(User actingUser)
    {
        // The user must be an employee in order to approve a job
        if(!(actingUser instanceof Employee))
            return false;
        
        // Only proposed jobs may be approved
        return this.status == JobStatus.Proposed;
    }

    public boolean canUserPostPayloads(User actingUser)
    {
        // If this job has not been approved, it cannot have payloads
        if(this.status != JobStatus.Approved || this.employee == null)
            return false;
        
        // Only employees may post payloads
        if(!(actingUser instanceof Employee))
            return false;
        
        // The user may post payloads if they accepted the job
        return this.employee.equals(actingUser);
    }
}
