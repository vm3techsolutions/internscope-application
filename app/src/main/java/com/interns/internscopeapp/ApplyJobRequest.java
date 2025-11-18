package com.interns.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class ApplyJobRequest {
    @SerializedName("resume_url")
    private String resume_url;

    @SerializedName("company_id")
    private int company_id;

//    @SerializedName("job_id")
//    private int jobId;

    public ApplyJobRequest(String resume_url, int company_id) {
        this.resume_url = resume_url;
        this.company_id = company_id;
//        this.jobId = jobId;
    }
}
