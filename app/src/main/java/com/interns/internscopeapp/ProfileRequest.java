package com.interns.internscopeapp;

public class ProfileRequest {
    private String firstName, lastName, location, phone, email, linkedIn, designation, qualification, description;

    public ProfileRequest(String firstName, String lastName, String location, String phone, String email,
                          String linkedIn, String designation, String qualification, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.linkedIn = linkedIn;
        this.designation = designation;
        this.qualification = qualification;
        this.description = description;
    }
}

