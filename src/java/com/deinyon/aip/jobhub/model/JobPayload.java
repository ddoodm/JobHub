/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.model;

import java.util.Date;
import java.util.UUID;

public class JobPayload
{
    private UUID id, jobId;
    private int version;
    private String details;
    private Date submissionDate;

    public JobPayload(UUID id, UUID jobId, int version, String details, Date submissionDate) {
        this.id = id;
        this.jobId = jobId;
        this.version = version;
        this.details = details;
        this.submissionDate = submissionDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
}
