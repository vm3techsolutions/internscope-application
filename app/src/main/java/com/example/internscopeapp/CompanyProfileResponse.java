package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class CompanyProfileResponse {

    @SerializedName("company_profile_id")
    private int companyProfileId;

    @SerializedName("company_id")
    private int companyId;

    @SerializedName("company_name")
    private String companyName;

    @SerializedName("company_type")
    private String companyType;

    @SerializedName("company_size")
    private String companySize;

    @SerializedName("email")
    private String email;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("location")
    private String location;

    @SerializedName("nation_state")
    private String nationState;

    @SerializedName("website")
    private String website;

    @SerializedName("twitter")
    private String twitter;

    @SerializedName("linkedin")
    private String linkedin;

    @SerializedName("kyc_method")
    private String kycMethod;

    @SerializedName("certificate_number")
    private String certificateNumber;

    @SerializedName("certificate_file_url")
    private String certificateFileUrl;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // ✅ Getters
    public int getCompanyProfileId() { return companyProfileId; }
    public int getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public String getCompanyType() { return companyType; }
    public String getCompanySize() { return companySize; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getLocation() { return location; }
    public String getNationState() { return nationState; }
    public String getWebsite() { return website; }
    public String getTwitter() { return twitter; }
    public String getLinkedin() { return linkedin; }
    public String getKycMethod() { return kycMethod; }
    public String getCertificateNumber() { return certificateNumber; }
    public String getCertificateFileUrl() { return certificateFileUrl; }
    public String getDescription() { return description; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // ✅ Setters
    public void setCompanyProfileId(int companyProfileId) { this.companyProfileId = companyProfileId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setCompanyType(String companyType) { this.companyType = companyType; }
    public void setCompanySize(String companySize) { this.companySize = companySize; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setLocation(String location) { this.location = location; }
    public void setNationState(String nationState) { this.nationState = nationState; }
    public void setWebsite(String website) { this.website = website; }
    public void setTwitter(String twitter) { this.twitter = twitter; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
    public void setKycMethod(String kycMethod) { this.kycMethod = kycMethod; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }
    public void setCertificateFileUrl(String certificateFileUrl) { this.certificateFileUrl = certificateFileUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
