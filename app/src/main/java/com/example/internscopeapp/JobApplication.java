package com.example.internscopeapp;


//public class JobApplication {
//    private int job_id;
//
//    public int getJob_id() { return job_id; }
//    public void setJob_id(int job_id) { this.job_id = job_id; }
//}

public class JobApplication {
    private int id;
    private int Job_id;
    private String status;
    private String applied_at;
    private String resume_url;
    private String job_title;
    private String location;
    private String companyName;

    public String getSalary_min() {
        return minSalary;
    }

    public void setSalary_min(String minSalary) {
        this.minSalary = minSalary;
    }

    private String minSalary;
    private String maxSalary;

    public String getSalary_max() {
        return maxSalary;
    }

    public void setSalary_max(String maxSalary) {
        this.maxSalary = maxSalary;
    }
// Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJob_id() {
        return Job_id;
    }

    public void setJob_id(int job_id) {
        this.Job_id = job_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplied_at() {
        return applied_at;
    }

    public void setApplied_at(String applied_at) {
        this.applied_at = applied_at;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getResume_url() {
        return resume_url;
    }

    public void setResume_url(String resume_url) {
        this.resume_url = resume_url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}


//public class JobApplication {
//    private int Job_id;
//    private int company_id;
//    private String job_title;
//    private String companyName;
//    private String location;
//    private String salary_min;
//    private String salary_max;
//    private String applied_at;
//    private String status;
//
//    public int getJob_id() {
//        return Job_id;
//    }
//
//    public int getCompany_id() {
//        return company_id;
//    }
//
//    public String getJob_title() {
//        return job_title;
//    }
//
//    public String getCompanyName() {
//        return companyName;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public String getSalary_min() {
//        return salary_min;
//    }
//
//    public String getSalary_max() {
//        return salary_max;
//    }
//
//    public String getApplied_at() {
//        return applied_at;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//}







