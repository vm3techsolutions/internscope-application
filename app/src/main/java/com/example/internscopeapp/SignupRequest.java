package com.example.internscopeapp;

public class SignupRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;

    public SignupRequest(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // getters & setters (optional)
}
