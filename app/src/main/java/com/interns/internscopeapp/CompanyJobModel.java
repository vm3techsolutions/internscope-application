package com.interns.internscopeapp;

//public class CompanyJobModel {
//    private int id;
//    private String title;
//    private String deadline;
//    private boolean isLive;
//
//    public CompanyJobModel(int id, String title, String deadline, boolean isLive) {
//        this.id = id;
//        this.title = title;
//        this.deadline = deadline;
//        this.isLive = isLive;
//    }
//
//    public int getId() { return id; }
//    public String getTitle() { return title; }
//    public String getDeadline() { return deadline; }
//    public boolean isLive() { return isLive; }
//}

//import com.google.gson.annotations.SerializedName;
//
//public class CompanyJobModel {
//
//    @SerializedName("Job_id")
//    private int jobId;
//
//    @SerializedName("job_title")
//    private String title;
//
//    private String deadline;
//
//    @SerializedName("is_live")
//    private boolean isLive;
//
//    public CompanyJobModel(int jobId, String title, String deadline, boolean isLive) {
//        this.jobId = jobId;
//        this.title = title;
//        this.deadline = deadline;
//        this.isLive = isLive;
//    }
//
//    public int getId() { return jobId; }
//    public String getTitle() { return title; }
//    public String getDeadline() { return deadline; }
//    public boolean isLive() { return isLive; }
//}



import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyJobModel {

    @SerializedName("Job_id")
    private int jobId;

    @SerializedName("company_id")
    private int companyId;

    @SerializedName("job_title")
    private String title;

    @SerializedName("industry")
    private String industry;

    @SerializedName("location")
    private String location;

    @SerializedName("qualification")
    private String qualification;

    @SerializedName("salary_min")
    private int salaryMin;

    @SerializedName("salary_max")
    private int salaryMax;

    @SerializedName("salary_type")
    private String salaryType;

    @SerializedName("job_type")
    private String jobType;

//    @SerializedName("job_tag")
//    private List<String> jobTag;

    private List<String> jobTag;

    @SerializedName("deadline")
    private String deadline;

    @SerializedName("job_description")
    private String description;

    @SerializedName("accepted_terms")
    private int acceptedTerms;

    @SerializedName("is_live")
    private boolean isLive;

    // 🟩 Constructor for job list (optional)
    public CompanyJobModel(int jobId, String title, String deadline, boolean isLive) {
        this.jobId = jobId;
        this.title = title;
        this.deadline = deadline;
        this.isLive = isLive;
    }

    // 🟩 Empty constructor (required by Gson)
    public CompanyJobModel() {}

    // 🟩 Getters and Setters
    public int getId() {
        return jobId;
    }

    public void setId(int jobId) {
        this.jobId = jobId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(int salaryMin) {
        this.salaryMin = salaryMin;
    }

    public int getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(int salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public List<String> getJobTag() {
        return jobTag;
    }

    public void setJobTag(List<String> jobTag) {
        this.jobTag = jobTag;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(int acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}
