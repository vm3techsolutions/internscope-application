package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class CandidateResponse {

    @SerializedName("application_id") // 👈 this should match your backend JSON key
    private int id;                   // job_applications.id

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("job_title")
    private String jobTitle;

    @SerializedName("applied_at")
    private String appliedDate;

    @SerializedName("resume_url")
    private String resumeUrl;

    @SerializedName("status")
    private String status;

    @SerializedName("job_id")
    private int jobId; // optional if needed later

    // ✅ Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getAppliedDate() { return appliedDate; }
    public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
