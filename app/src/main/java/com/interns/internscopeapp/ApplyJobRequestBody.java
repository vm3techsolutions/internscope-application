package com.interns.internscopeapp;

public class ApplyJobRequestBody {
    private int company_id;

    public ApplyJobRequestBody(int company_id) {
        this.company_id = company_id;
    }

    public int getCompany_id() {
        return company_id;
    }
}
