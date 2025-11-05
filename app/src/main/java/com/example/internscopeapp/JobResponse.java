package com.example.internscopeapp;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JobResponse {

    @SerializedName("Job_id")
    private int jobId;

    @SerializedName("company_id")
    private int companyId;

    @SerializedName("job_title")
    private String jobTitle;

    @SerializedName("companyName")
    private String companyName;

    @SerializedName("location")
    private String location;

    @SerializedName("salary_min")
    private int salaryMin;

    @SerializedName("salary_max")
    private int salaryMax;

    @SerializedName("job_type")
    private String jobType;

    @SerializedName("qualification")
    private String qualification;

    @SerializedName("job_description")
    private String jobDescription;

    @SerializedName("job_tag")
    @JsonAdapter(JobTagDeserializer.class) // Custom deserializer
    private List<String> jobTag;

    // --- Getters ---
    public int getJobId() { return jobId; }
    public int getCompanyId() { return companyId; }
    public String getJobTitle() { return jobTitle; }
    public String getCompanyName() { return companyName; }
    public String getLocation() { return location; }
    public int getSalaryMin() { return salaryMin; }
    public int getSalaryMax() { return salaryMax; }
    public String getJobType() { return jobType; }
    public String getQualification() { return qualification; }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalaryMin(int salaryMin) {
        this.salaryMin = salaryMin;
    }

    public void setSalaryMax(int salaryMax) {
        this.salaryMax = salaryMax;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setJobTag(List<String> jobTag) {
        this.jobTag = jobTag;
    }

    public String getJobDescription() { return jobDescription; }
    public List<String> getJobTag() { return jobTag; }
}
