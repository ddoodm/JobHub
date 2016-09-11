package com.deinyon.aip.jobhub.controllers;

import com.deinyon.aip.jobhub.database.PayloadDAO;
import com.deinyon.aip.jobhub.model.JobPayload;
import java.io.IOException;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * A Controller Bean which interfaces the 'Edit Payload' Facelet with the
 * model and database.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@SessionScoped
@ManagedBean(name = "editPayloadController")
public class EditPayloadController extends GenericController
{
    /**
     * The presently active payload which is being edited.
     */
    private JobPayload payload;
    
    /**
     * @return The presently active payload which is being edited.
     */
    public JobPayload getPayload()
    {
        return payload;
    }
    
    /**
     * Loads the Payload with the given ID from the database, as the controller's
     * currently active Payload.
     * @param payloadId The ID of the Payload to load from the database
     * @throws IOException If the Payload could not be found, or if a database
     * error prevents the query from executing.
     */
    private void loadPayload(UUID payloadId) throws IOException
    {
        try(PayloadDAO dao = new PayloadDAO())
        {
            this.payload = dao.find(payloadId);
        }
    }
    
    /**
     * Loads the Payload with the given ID from the database, as the controller's
     * currently active Payload.
     * @param payloadIdString The ID of the Payload to load from the database,
     * represented by a String value.
     * @throws IOException If the Payload could not be found, or if a database
     * error prevents the query from executing.
     */
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
    
    /**
     * Saves the details of the presently active Payload into the database.
     * @return The outcome to which the server should redirect.
     * @throws IOException If a database error prevents the Payload from being
     * updated, or if the Payload does not already exist on the database.
     */
    public String updatePayload() throws IOException
    {
        try(PayloadDAO dao = new PayloadDAO())
        {
            dao.update(payload);
        }
        
        return "jobs";
    }
}
