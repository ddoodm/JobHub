/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.database.JobDAO;
import com.deinyon.aip.jobhub.database.PayloadDAO;
import com.deinyon.aip.jobhub.model.JobPayload;
import java.io.IOException;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "editPayloadController")
public class EditPayloadController extends GenericController
{
    private JobPayload payload;
    
    public JobPayload getPayload()
    {
        return payload;
    }
    
    private void loadPayload(UUID payloadId) throws IOException
    {
        try(PayloadDAO dao = new PayloadDAO())
        {
            this.payload = dao.find(payloadId);
        }
    }
    
    public void loadPayload(String payloadIdString) throws IOException
    {
        try
        {
            UUID payloadId = UUID.fromString(payloadIdString);
            loadPayload(payloadId);
        }
        catch (IllegalArgumentException e)
        {
            // Handles cases where the Payload ID string is null or invalid
            this.payload = null;
        }
    }
    
    public String updatePayload() throws IOException
    {
        try(PayloadDAO dao = new PayloadDAO())
        {
            dao.update(payload);
        }
        
        return "jobs";
    }
}
