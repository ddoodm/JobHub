package com.deinyon.aip.jobhub.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

/**
 * The Business Object and Data Transfer Object which represents extended Job details
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class JobDescription implements Serializable
{
    /**
     * The ID of this Job Description
     */
    private UUID id;
    
    /**
     * The Title of the Job
     */
    private String title;

    /**
     * The message outlining the details of the Job
     */
    private String details;

    /**
     * The Date when the Job was first created (and listed)
     */
    private Date listingDate;
    
    /**
     * The Date, in the future, when the Job should be completed
     */
    private Date targetEndDate;

    /**
     * The amount that an Employee should be paid
     */
    private BigDecimal payment;
    
    /**
     * The path to the associated attachment file (not used)
     */
    private String attachmentFilePath;

    /**
     * Creates a new Job Description
     */
    public JobDescription() { }

    /**
     * Creates a new Job Description with known parameters
     * @param id The ID of this Job Description
     * @param title The Title of the Job
     * @param details The message outlining the details of the Job
     * @param payment The amount that an Employee should be paid
     * @param listingDate The Date when the Job was first created (and listed)
     * @param targetEndDate The Date, in the future, when the Job should be completed
     */
    public JobDescription(UUID id, String title, String details, BigDecimal payment, Date listingDate, Date targetEndDate) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.payment = payment;
        this.listingDate = listingDate;
        this.targetEndDate = targetEndDate;
    }

    /**
     * Utility function for creating a new Job Description with some pre-initialized parameters
     * @param title The Title of the Job
     * @param details The message outlining the details of the Job
     * @param payment The amount that an Employee should be paid
     * @param targetEndDate The Date, in the future, when the Job should be completed
     * @return A new Job Description with some pre-initialized parameters
     */
    public static JobDescription CreateJobDescription(String title, String details, BigDecimal payment, Date targetEndDate)
    {
        Date listingDate = new Date();
        UUID id = UUID.randomUUID();
        return new JobDescription(id, title, details, payment, listingDate, targetEndDate);
    }
    
    /**
     * Initializes a Job Description for inserting into the database
     */
    public void prepare()
    {
        // Generate a new ID
        this.id = UUID.randomUUID();
        
        // Set the job's listing date to now
        setListingDate(new Date());
    }
    
    /**
     * @return The ID of this Job Description
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return The Title of the Job
     */
    @NotEmpty(message = "Please enter a title for your job")
    @Length(min = 3, max = 120, message = "Please enter a meaningful title below 120 characters")
    public String getTitle() {
        return title;
    }
    
    /**
     * @param title The Title of the Job
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The message outlining the details of the Job
     */
    @NotEmpty(message = "Please give your job a short description")
    @Length(min = 3, max = 10000, message = "Please enter a meaningful description below 10,000 characters")
    public String getDetails() {
        return details;
    }
    
    /**
     * @param details The message outlining the details of the Job
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return The Date when the Job was first created (and listed)
     */
    public Date getListingDate() {
        return listingDate;
    }
    
    /**
     * @param listingDate The Date when the Job was first created (and listed)
     */
    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }

    /**
     * @return The Date, in the future, when the Job should be completed
     */
    @Future(message = "Please select a date in the future")
    @NotNull(message = "Please specify when you want the job completed")
    public Date getTargetEndDate() {
        return targetEndDate;
    }
    
    /**
     * @param targetEndDate The Date, in the future, when the Job should be completed
     */
    public void setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }
    
    /**
     * @return The amount that an Employee should be paid
     */
    @NotNull(message = "Please specify how much you want to pay")
    @Min(value = 1, message = "You must pay at least $1.00")
    public BigDecimal getPayment() {
        return payment;
    }
    
    /**
     * @param payment The amount that an Employee should be paid
     */
    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    /**
     * @return The path to the associated attachment file (not used)
     */
    public String getAttachmentFilePath() {
        return attachmentFilePath;
    }

    /**
     * @param attachmentFilePath The path to the associated attachment file (not used)
     */
    public void setAttachmentFilePath(String attachmentFilePath) {
        this.attachmentFilePath = attachmentFilePath;
    }
}
