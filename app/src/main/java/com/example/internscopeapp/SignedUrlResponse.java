package com.example.internscopeapp;

public class SignedUrlResponse {
    private String uploadUrl;
    private String fileUrl;
    private String s3Key;

    public String getUploadUrl() { return uploadUrl; }
    public String getFileUrl() { return fileUrl; }
    public String getS3Key() { return s3Key; }
}
