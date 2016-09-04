package com.deinyon.aip.jobhub;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

public class JobDescription implements Serializable
{
    private UUID id;
    
    private String title;

    private String details;

    private Date
            listingDate,
            targetEndDate;

    private BigDecimal payment;
    
    private String attachmentFilePath;

    public JobDescription() { }

    public JobDescription(UUID id, String title, String details, BigDecimal payment, Date listingDate, Date targetEndDate) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.payment = payment;
        this.listingDate = listingDate;
        this.targetEndDate = targetEndDate;
    }

    public static JobDescription CreateJobDescription(String title, String details, BigDecimal payment, Date targetEndDate)
    {
        Date listingDate = new Date();
        UUID id = UUID.randomUUID();
        return new JobDescription(id, title, details, payment, listingDate, targetEndDate);
    }

    @NotEmpty(message = "Please enter a title for your job")
    @Length(min = 3, max = 120, message = "Please enter a meaningful title below 120 characters")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @NotEmpty(message = "Please give your job a short description")
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public Date getListingDate() {
        return listingDate;
    }
    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }

    @Future(message = "Please select a date in the future")
    @NotNull(message = "Please specify when you want the job completed")
    public Date getTargetEndDate() {
        return targetEndDate;
    }
    public void setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }
    
    @NotNull(message = "Please specify how much you want to pay")
    @Min(value = 1, message = "You must pay at least $1.00")
    public BigDecimal getPayment() {
        return payment;
    }
    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getAttachmentFilePath() {
        return attachmentFilePath;
    }

    public void setAttachmentFilePath(String attachmentFilePath) {
        this.attachmentFilePath = attachmentFilePath;
    }
}
