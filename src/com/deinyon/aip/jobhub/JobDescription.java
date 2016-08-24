package com.deinyon.aip.jobhub;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ManagedBean(name = "jobDescription")
@SessionScoped
public class JobDescription
{
    private String title;

    private String details;

    private LocalDateTime
            listingDate,
            targetEndDate;

    private BigDecimal payment;

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public JobDescription() { }

    public JobDescription(String title, String details, LocalDateTime listingDate, LocalDateTime targetEndDate) {
        this.title = title;
        this.details = details;
        this.listingDate = listingDate;
        this.targetEndDate = targetEndDate;
    }

    public static JobDescription CreateJobDescription(String title, String details, LocalDateTime targetEndDate)
    {
        LocalDateTime listingDate = LocalDateTime.now();
        return new JobDescription(title, details, listingDate, targetEndDate);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getListingDate() {
        return listingDate;
    }

    public void setListingDate(LocalDateTime listingDate) {
        this.listingDate = listingDate;
    }

    public LocalDateTime getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(LocalDateTime targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    public String getListingDateFormatted() {
        if(listingDate == null)
            return "-";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM uuuu");
        return listingDate.format(formatter);
    }
}
