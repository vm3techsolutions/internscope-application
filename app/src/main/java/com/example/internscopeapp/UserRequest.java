package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class UserRequest {

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

    public UserRequest(String firstName, String lastName, String currentLocation, String nationState,
                       String phone, String email, String linkedin, String currentJobPlace,
                       String designation, String qualification, String language, String resume,
                       String gender, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentLocation = currentLocation;
        this.nationState = nationState;
        this.phone = phone;
        this.email = email;
        this.linkedin = linkedin;
        this.currentJobPlace = currentJobPlace;
        this.designation = designation;
        this.qualification = qualification;
        this.language = language;
        this.resume = resume;
        this.gender = gender;
        this.description = description;
    }

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
    public String getResume() { return resume; }
    public String getGender() { return gender; }
    public String getDescription() { return description; }
}
