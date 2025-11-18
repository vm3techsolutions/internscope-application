package com.interns.internscopeapp;

// FileRequestBody.java
public class FileRequestBody {
    private String fileName;
    private String contentType;

    public FileRequestBody(String fileName, String contentType) {
        this.fileName = fileName;
        this.contentType = contentType;
    }
}

