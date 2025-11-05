package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class Plan {

    private int id;

    private String name;

    @SerializedName("user_type")
    private String userType;

    private double price;

    @SerializedName("duration_days")
    private int durationDays;

    @SerializedName("job_post_limit")
    private int jobPostLimit;

    @SerializedName("is_combo")
    private int isCombo;

    private String description;

    @SerializedName("gst_rate")
    private double gstRate;

    // Constructor
    public Plan() {}

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getJobPostLimit() {
        return jobPostLimit;
    }

    public void setJobPostLimit(int jobPostLimit) {
        this.jobPostLimit = jobPostLimit;
    }

    public int getIsCombo() {
        return isCombo;
    }

    public void setIsCombo(int isCombo) {
        this.isCombo = isCombo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getGstRate() {
        return gstRate;
    }

    public void setGstRate(double gstRate) {
        this.gstRate = gstRate;
    }

    // Optional: method to calculate final price with GST
    public double getPriceWithGst() {
        return price + (price * gstRate / 100);
    }
}