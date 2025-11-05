//package com.example.internscopeapp;
//
//import com.google.gson.annotations.SerializedName;
//
//public class AppliedJobResponse {
//
//    @SerializedName("Job_id")   // <-- match backend JSON key
//    private int jobId;
//
//    public int getJobId() {
//        return jobId;
//    }
//}


//package com.example.internscopeapp;
//
//import com.google.gson.annotations.SerializedName;
//
//public class AppliedJobResponse {
//
//    @SerializedName("job_id")
//    private int jobId;
//
//    @SerializedName("job_title")
//    private String jobTitle;
//
//    @SerializedName("company_id")
//    private int companyId;
//
//    @SerializedName("companyName")
//    private String companyName;
//
//    @SerializedName("location")
//    private String location;
//
//    @SerializedName("status")           // Applied status
//    private String status;
//
//    @SerializedName("applied_at")       // Applied date
//    private String appliedAt;
//
//    // Add getters and setters
//    public int getJobId() { return jobId; }
//    public void setJobId(int jobId) { this.jobId = jobId; }
//
//    public String getJobTitle() { return jobTitle; }
//    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
//
//    public int getCompanyId() { return companyId; }
//    public void setCompanyId(int companyId) { this.companyId = companyId; }
//
//    public String getCompanyName() { return companyName; }
//    public void setCompanyName(String companyName) { this.companyName = companyName; }
//
//    public String getLocation() { return location; }
//    public void setLocation(String location) { this.location = location; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public String getAppliedAt() { return appliedAt; }
//    public void setAppliedAt(String appliedAt) { this.appliedAt = appliedAt; }
//
//
//}

package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class AppliedJobResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("job_id")
    private int jobId;

    @SerializedName("status")
    private String status;

    @SerializedName("applied_at")
    private String appliedAt;

    @SerializedName("resume_url")
    private String resumeUrl;

    @SerializedName("job_title")
    private String jobTitle;

    @SerializedName("location")
    private String location;

    @SerializedName("salary_min")
    private String salaryMin;

    @SerializedName("salary_max")
    private String salaryMax;

    @SerializedName("companyName")
    private String companyName;

    // ✅ Getters
    public int getId() { return id; }
    public int getJobId() { return jobId; }
    public String getStatus() { return status; }
    public String getAppliedAt() { return appliedAt; }
    public String getResumeUrl() { return resumeUrl; }
    public String getJobTitle() { return jobTitle; }
    public String getLocation() { return location; }
    public String getSalaryMin() { return salaryMin; }

    public void setId(int id) {
        this.id = id;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAppliedAt(String appliedAt) {
        this.appliedAt = appliedAt;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalaryMin(String salaryMin) {
        this.salaryMin = salaryMin;
    }

    public void setSalaryMax(String salaryMax) {
        this.salaryMax = salaryMax;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSalaryMax() { return salaryMax; }
    public String getCompanyName() { return companyName; }
}
