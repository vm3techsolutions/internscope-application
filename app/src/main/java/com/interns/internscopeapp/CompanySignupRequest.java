package com.interns.internscopeapp;

public class CompanySignupRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String companyName;
    private String companyType;

    public CompanySignupRequest(String firstName, String lastName, String username, String email, String password, String companyName, String companyType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.companyType = companyType;
    }
}
