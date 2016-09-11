package com.deinyon.aip.jobhub.model;

import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.hibernate.validator.constraints.*;

/**
 * The Business Object and Data Transfer Object which represents a Job on JobHub
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class Job implements Serializable
{
    /**
     * The identifier of this job.
     */
    private UUID id;
    
    /**
     * Provides extended information about the job.
     */
    private JobDescription description;
    
    /**
     * The user who has taken the job, and who may post Payloads to it. May be null.
     */
    private Employee employee;
    
    /**
     * The User who listed (created) the job, and is responsible for maintaining it.
     */
    private Employer employer;
    
    /**
     * The state of this job.
     */
    private JobStatus status;
    
    /**
     * The responses that this job has received from its Employee.
     */
    private Collection<JobPayload> payloads;

    /**
     * Creates a new Job.
     */
    public Job()
    {
        description = new JobDescription();
        status = JobStatus.Proposed;
        payloads = new ArrayList<>();
    }
    
    /**
     * Creates a new Job with known properties.
     * @param id The identifier of this job.
     * @param employer The User who listed (created) the job, and is responsible for maintaining it.
     * @param description Provides extended information about the job.
     * @param status The state of this job.
     * @param payloads The responses that this job has received from its Employee.
     */
    public Job(UUID id, Employer employer, JobDescription description, JobStatus status, Collection<JobPayload> payloads)
    {
        this.id = id;
        this.employer = employer;
        this.description = description;
        this.status = status;
        this.payloads = payloads;
    }

    /**
     * Creates a new Job with known extended values.
     * @param description Provides extended information about the job.
     */
    public Job(JobDescription description)
    {
        this.description = description;
    }
    
    /**
     * Called when the job's details have been written,
     * and before the job is inserted into a database.
     * Writes additional properties to the job.
     */
    public void prepare()
    {
        description.prepare();
        
        // Generate a new ID
        id = UUID.randomUUID();
    }
    
    /**
     * Associates this Job with an Employee so that the Employee is permitted to
     * submit Payloads to the Job.
     * @param employee The Employee who should be associated with this Job.
     */
    public void delegateTo(Employee employee)
    {
        this.setEmployee(employee);
        this.status = JobStatus.Approved;
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

    /**
     * @param actingUser The user who is currently logged-in
     * @return True if the specified user is permitted to submit Payloads to
     * this Job listing.
     */
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

    /**
     * @param actingUser The user who is currently logged-in
     * @return True if the specified user is permitted to view Payloads on this
     * Job listing.
     */
    public boolean canUserSeePayloads(User actingUser)
    {
        // A proposed job cannot have payloads
        if(this.status == JobStatus.Proposed)
            return false;
        
        // Payloads are shared between the Employee and Employer only
        return this.employee.equals(actingUser) || this.employer.equals(actingUser);
    }
    
    /**
     * @param actingUser The user who is currently logged-in
     * @return True if this job has been taken by a user other than the specified
     * user, and the job has passed its 'Proposed' stage.
     */
    public boolean isJobTakenByOtherEmployee(User actingUser)
    {
        // If this job is proposed, it is not taken by anyone
        if(this.status == JobStatus.Proposed)
            return false;
        
        // If the user is not an employee, we cannot make the comparison
        if(!(actingUser instanceof Employee))
            return false;
        
        // If the acting user is not the job's employee, the job is taken by another employee
        return !this.employee.equals(actingUser);
    }
    
    /**
     * @param actingUser The user who is currently logged-in
     * @return True if the specified user is permitted to close this job
     */
    public boolean isUserPermittedToCloseJob(User actingUser)
    {
        // Only the Employer who listed the job is permitted to close it,
        // but only if it is currently approved
        if(this.status != JobStatus.Approved)
            return false;
        
        return this.employer.equals(actingUser);
    }
    
    /**
     * @return True if this job has no payloads. False otherwise.
     */
    public boolean isPayloadsEmpty()
    {
        return this.payloads == null || this.payloads.size() < 1;
    }

    /**
     * Adds a payload to this Job
     * @param newPayload The Payload to submit to this Job
     */
    public void addPayload(JobPayload newPayload)
    {
        this.payloads.add(newPayload);
    }
    
    /**
     * Removes a Payload from this Job
     * @param payload The Payload to remove from this Job
     */
    public void removePayload(JobPayload payload)
    {
        this.payloads.remove(payload);
    }
    
    /**
     * @return The collection of payloads associated with this Job
     */
    public Collection<JobPayload> getPayloads()
    {
        return this.payloads;
    }

    /**
     * @param user The user who is currently logged-in
     * @return True if this job was listed by the specified user, false otherwise.
     */
    public boolean wasCreatedBy(User user)
    {
        // If the user is not an employer, they could not have created the job
        if(user == null || !(user instanceof Employer))
            return false;
        
        // If the Employer is the user, the user is the Employer
        return getEmployer().equals(user);
    }
    
    /**
     * Removes this job from all job listings, but does not delete it.
     */
    public void close()
    {
        this.status = JobStatus.Closed;
    }
    
    /**
     * @return The human-readable status of this Job
     */
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

    /**
     * @return Extended information about the job.
     */
    public JobDescription getDescription() {
        return description;
    }

    /**
     * @param description Extended information about the job.
     */
    public void setDescription(JobDescription description) {
        this.description = description;
    }

    /**
     * @return The user who has taken the job, and who may post Payloads to it. May be null.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee The user who has taken the job, and who may post Payloads to it. May be null.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return The User who listed (created) the job, and is responsible for maintaining it.
     */
    public Employer getEmployer() {
        return employer;
    }

    /**
     * @param employer The User who listed (created) the job, and is responsible for maintaining it.
     */
    public void setEmployer(Employer employer) {
        this.employer = employer;
    }
    
    /**
     * @return The state of this job.
     */
    public JobStatus getStatus() {
        return status;
    }

    /**
     * @param status The state of this job.
     */
    public void setStatus(JobStatus status) {
        this.status = status;
    }

    /**
     * @return The identifier of this job.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id The identifier of this job.
     */
    public void setId(UUID id) {
        this.id = id;
    }
}
