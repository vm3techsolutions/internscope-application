package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class Plan {

    private int id;
    private String name;

    @SerializedName("user_type")
    private String userType; // "student" or "company"

    private double price;

    @SerializedName("duration_days")
    private int durationDays;

    // Optional: applies mostly to company plans
    @SerializedName("job_post_limit")
    private Integer jobPostLimit;

    @SerializedName("is_combo")
    private Integer isCombo;

    private String description;

    @SerializedName("gst_rate")
    private Double gstRate;

    // Optional: common field for display or extended data
    @SerializedName("features")
    private String features;

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

    public Integer getJobPostLimit() {
        return jobPostLimit;
    }

    public void setJobPostLimit(Integer jobPostLimit) {
        this.jobPostLimit = jobPostLimit;
    }

    public Integer getIsCombo() {
        return isCombo;
    }

    public void setIsCombo(Integer isCombo) {
        this.isCombo = isCombo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getGstRate() {
        return gstRate;
    }

    public void setGstRate(Double gstRate) {
        this.gstRate = gstRate;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    // ✅ Method to calculate price including GST (handles nulls safely)
    public double getPriceWithGst() {
        if (gstRate == null) return price;
        return price + (price * gstRate / 100);
    }
}
