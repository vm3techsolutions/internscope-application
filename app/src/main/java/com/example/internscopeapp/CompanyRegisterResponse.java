package com.example.internscopeapp;

public class CompanyRegisterResponse {
    private boolean success;
    private String message;
    private Data data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Data getData() { return data; }

    public static class Data {
        private int companyId; // must match backend JSON key

        public int getCompanyId() { return companyId; }
    }
}
