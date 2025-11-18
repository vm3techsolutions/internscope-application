package com.interns.internscopeapp;

// SignedUrlResponse.java
//public class SignedUrlResponse {
//    private String signedUrl;
//    private String fileUrl;
//
//    public String getSignedUrl() { return signedUrl; }
//    public String getFileUrl() { return fileUrl; }
//}


import com.google.gson.annotations.SerializedName;

public class SignedUrlResponse {

    @SerializedName("signedUrl")
    private String signedUrl;

    @SerializedName("s3Key")
    private String s3Key;

    public String getSignedUrl() {
        return signedUrl;
    }

    public String getS3Key() {
        return s3Key;
    }
}

