package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.database.PayloadDAO;
import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.model.JobPayload;
import com.deinyon.aip.jobhub.users.Employee;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * The Controller Bean which deals with saving and deleting payloads. This
 * controller is used exclusively by the Job view.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@SessionScoped
@ManagedBean(name = "payloadController")
public class PayloadController extends GenericController
{
    /**
     * An empty payload which can be populated and inserted into the database.
     */
    private JobPayload newPayload = new JobPayload();
    
    /**
     * @return An empty payload which can be populated and inserted into the database.
     */
    public JobPayload getNewPayload()
    {
        return newPayload;
    }
    
    /**
     * Prepares and saves a payload to the database.
     * @remarks The function makes associations between the payload and its
     * related Job and Author.
     * @param job The Job for which this payload is being posted.
     * @throws IOException If a database error prevented the payload from being
     * saved.
     */
    public void postPayload(Job job) throws IOException
    {
        // We must prepare the payload for saving to the database
        newPayload.prepare();
        
        // Set the ID of the payload's parent Job
        newPayload.setJobId(job.getId());
        
        // Set the user who is authoring the new payload
        newPayload.setAuthor(getUserController().<Employee>getActingUserTyped());
        
        // Make the job aware of the new payload
        job.addPayload(newPayload);
        
        // Save the payload to the database
        try(PayloadDAO dao = new PayloadDAO())
        {
            dao.save(newPayload);
        }
        
        // Restore the new payload
        newPayload = new JobPayload();
    }
    
    /**
     * Removes a payload from the database, and from the in-memory instance
     * of its associated job.
     * @param payload An instance of a Payload to remove from the database.
     * @param job The Job with which this Payload is associated.
     * @throws IOException If a database error prevented the payload from being
     * deleted.
     */
    public void delete(JobPayload payload, Job job) throws IOException
    {
        // Remove the payload from the job
        job.removePayload(payload);
        
        // Remove the payload from the database
        try(PayloadDAO dao = new PayloadDAO())
        {
            dao.delete(payload);
        }
    }
}
