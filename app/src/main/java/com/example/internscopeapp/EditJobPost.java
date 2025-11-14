package com.example.internscopeapp;

import java.util.List;

public class EditJobPost {
    private int Job_id;
    private int company_id;
    private String job_title;
    private String industry;
    private String location;
    private String qualification;
    private Integer salary_min;
    private Integer salary_max;
    private String salary_type;
    private String job_type;
    private List<String> job_tag;
    private String deadline;
    private String job_description;

    // ✅ Getters and Setters
    public int getJob_id() { return Job_id; }
    public void setJob_id(int job_id) { Job_id = job_id; }

    public String getJob_title() { return job_title; }
    public void setJob_title(String job_title) { this.job_title = job_title; }

    public List<String> getJob_tag() { return job_tag; }
    public void setJob_tag(List<String> job_tag) { this.job_tag = job_tag; }

    public String getJob_type() { return job_type; }
    public void setJob_type(String job_type) { this.job_type = job_type; }

    public Integer getSalary_min() { return salary_min; }
    public void setSalary_min(Integer salary_min) { this.salary_min = salary_min; }

    public Integer getSalary_max() { return salary_max; }
    public void setSalary_max(Integer salary_max) { this.salary_max = salary_max; }

    public String getSalary_type() { return salary_type; }
    public void setSalary_type(String salary_type) { this.salary_type = salary_type; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getJob_description() { return job_description; }
    public void setJob_description(String job_description) { this.job_description = job_description; }
}
