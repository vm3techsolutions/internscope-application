package com.interns.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class JobDetail {
    @SerializedName("Job_id")
    private int jobId;

    @SerializedName("job_title")
    private String jobTitle;

    @SerializedName("location")
    private String location;

    @SerializedName("status")
    private String status;

    @SerializedName("companyName")
    private String companyName;

    public int getJobId() { return jobId; }
    public String getJobTitle() { return jobTitle; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public String getCompanyName() { return companyName; }
}
