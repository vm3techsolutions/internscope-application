package com.example.internscopeapp;

public class ApplyJobRequest {
    private String resume_url;
    private int company_id;

    public ApplyJobRequest(String resume_url, int company_id) {
        this.resume_url = resume_url;
        this.company_id = company_id;
    }
}
