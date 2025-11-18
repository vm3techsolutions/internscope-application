package com.interns.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("current_location")
    private String currentLocation;

    @SerializedName("nation_state")
    private String nationState;

    @SerializedName("phone_number")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("website_link")
    private String linkedin;

    @SerializedName("current_job_place")
    private String currentJobPlace;

    @SerializedName("designation")
    private String designation;

    @SerializedName("qualification")
    private String qualification;

    @SerializedName("language")
    private String language;

    @SerializedName("resume")
    private String resume;
    @SerializedName("gender")
    private String gender;

    @SerializedName("description")
    private String description;

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCurrentLocation() { return currentLocation; }
    public String getNationState() { return nationState; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getLinkedin() { return linkedin; }
    public String getCurrentJobPlace() { return currentJobPlace; }
    public String getDesignation() { return designation; }
    public String getQualification() { return qualification; }
    public String getLanguage() { return language; }
    public String getGender() { return gender; }
    public String getResume(){return  resume;}
    public String getDescription() { return description; }
}
