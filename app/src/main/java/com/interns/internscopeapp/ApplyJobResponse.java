package com.interns.internscopeapp;

public class ApplyJobResponse {
    private String message;
    private Data data;

    public String getMessage() { return message; }
    public Data getData() { return data; }

    public class Data {
        private int jobId;
        private int userId;

        private int companyId;
        private String resumeUrl;

        public int getJobId() { return jobId; }
        public int getUserId() { return userId; }

        public int getCompanyId() {return companyId;}

        public String getResumeUrl() { return resumeUrl; }
    }
}
