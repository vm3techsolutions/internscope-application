package com.interns.internscopeapp;

import java.util.List;

public class JobPostRequest {
    private int company_id;
    private String job_title;
    private String industry;
    private String location;
    private String qualification;
    private int salary_min;
    private int salary_max;
    private String salary_type;
    private String job_type;
    private List<String> job_tag;
    private String deadline;
    private String job_description;
    private boolean accepted_terms;

    public JobPostRequest(int company_id, String job_title, String industry, String location,
                          String qualification, int salary_min, int salary_max,
                          String salary_type, String job_type, List<String> job_tag,
                          String deadline, String job_description, boolean accepted_terms) {
        this.company_id = company_id;
        this.job_title = job_title;
        this.industry = industry;
        this.location = location;
        this.qualification = qualification;
        this.salary_min = salary_min;
        this.salary_max = salary_max;
        this.salary_type = salary_type;
        this.job_type = job_type;
        this.job_tag = job_tag;
        this.deadline = deadline;
        this.job_description = job_description;
        this.accepted_terms = accepted_terms;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
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

    public int getSalary_min() {
        return salary_min;
    }

    public void setSalary_min(int salary_min) {
        this.salary_min = salary_min;
    }

    public int getSalary_max() {
        return salary_max;
    }

    public void setSalary_max(int salary_max) {
        this.salary_max = salary_max;
    }

    public String getSalary_type() {
        return salary_type;
    }

    public void setSalary_type(String salary_type) {
        this.salary_type = salary_type;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public List<String> getJob_tag() {
        return job_tag;
    }

    public void setJob_tag(List<String> job_tag) {
        this.job_tag = job_tag;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public boolean isAccepted_terms() {
        return accepted_terms;
    }

    public void setAccepted_terms(boolean accepted_terms) {
        this.accepted_terms = accepted_terms;
    }
}
