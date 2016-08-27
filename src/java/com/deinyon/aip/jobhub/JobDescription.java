package com.deinyon.aip.jobhub;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

public class JobDescription implements Serializable
{
    private String title;

    private String details;

    private Date
            listingDate,
            targetEndDate;

    private BigDecimal payment;

    public JobDescription() { }

    public JobDescription(String title, String details, Date listingDate, Date targetEndDate) {
        this.title = title;
        this.details = details;
        this.listingDate = listingDate;
        this.targetEndDate = targetEndDate;
    }

    public static JobDescription CreateJobDescription(String title, String details, Date targetEndDate)
    {
        Date listingDate = new Date();
        return new JobDescription(title, details, listingDate, targetEndDate);
    }

    @NotEmpty(message = "Please enter a title for your job")
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

    public String getListingDateFormatted() {
        if(listingDate == null)
            return "-";

        SimpleDateFormat formatter = new SimpleDateFormat("d MMMM uuuu");
        return formatter.format(listingDate);
    }
}
