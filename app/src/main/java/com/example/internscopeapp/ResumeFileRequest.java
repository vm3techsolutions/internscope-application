package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class ResumeFileRequest {

    @SerializedName("fileName")
    private String fileName;

    public ResumeFileRequest(String fileName) {
        this.fileName = fileName;
    }
}


