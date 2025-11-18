//package com.interns.internscopeapp;
//
//public class UserProfile {
//    private String first_name;
//    private String last_name;
//    private String current_job_place;
//    private String designation;
//    private String description;
//
//    private String resumeUrl;
//
//    // Getters
//    public String getFirstName() { return first_name; }
//    public String getLastName() { return last_name; }
//    public String getCurrentJobPlace() { return current_job_place; }
//    public String getDesignation() { return designation; }
//    public String getDescription() { return description; }
//
//    public java.lang.String getResumeUrl() {
//        return resumeKey;
//    }
//}

package com.interns.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("current_job_place")
    private String currentJobPlace;

    @SerializedName("designation")
    private String designation;

    @SerializedName("description")
    private String description;

    @SerializedName("resume")     // <-- IMPORTANT, backend uses "resume"
    private String resumeKey;

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCurrentJobPlace() { return currentJobPlace; }
    public String getDesignation() { return designation; }
    public String getDescription() { return description; }

    public String getResume() { return resumeKey; } // <-- Correct getter
}
