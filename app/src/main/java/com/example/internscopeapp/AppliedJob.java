package com.example.internscopeapp;

public class AppliedJob {
    private int job_id;
    private String job_title;
    private String company_name;
    private String location;
    private String status;
    private String apply_date;

    //new added for user dashboard
    private String salary_min;
    private String salary_max;

    public int getJob_id() { return job_id; }
    public String getJob_title() { return job_title; }
    public String getCompany_name() { return company_name; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }

    //new added for user dashboard
    public String getSalary_min() { return salary_min; }
    public String getSalary_max() { return salary_max; }
    public String getApply_date() { return apply_date; }
}
