/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.database.PayloadDAO;
import com.deinyon.aip.jobhub.model.Job;
import com.deinyon.aip.jobhub.model.JobPayload;
import com.deinyon.aip.jobhub.users.Employee;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "payloadController")
public class PayloadController extends GenericController
{
    private JobPayload newPayload = new JobPayload();
    
    public JobPayload getNewPayload()
    {
        return newPayload;
    }
    
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
