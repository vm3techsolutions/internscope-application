package com.interns.internscopeapp;
//
//public class CompanyProfileRequest {
//    private String company_name;
//    private String company_type;
//    private String company_size;
//    private String email;
//    private String phone_number;
//    private String location;
//    private String nation_state;
//    private String website;
//    private String twitter;
//    private String linkedin;
//    private String kyc_method;
//    private String certificate_number;
//    private String certificate_file_url;
//    private String description;
//
//    public String getCompany_name() {
//        return company_name;
//    }
//
//    public void setCompany_name(String company_name) {
//        this.company_name = company_name;
//    }
//
//    public String getCompany_type() {
//        return company_type;
//    }
//
//    public void setCompany_type(String company_type) {
//        this.company_type = company_type;
//    }
//
//    public String getCompany_size() {
//        return company_size;
//    }
//
//    public void setCompany_size(String company_size) {
//        this.company_size = company_size;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone_number() {
//        return phone_number;
//    }
//
//    public void setPhone_number(String phone_number) {
//        this.phone_number = phone_number;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getNation_state() {
//        return nation_state;
//    }
//
//    public void setNation_state(String nation_state) {
//        this.nation_state = nation_state;
//    }
//
//    public String getWebsite() {
//        return website;
//    }
//
//    public void setWebsite(String website) {
//        this.website = website;
//    }
//
//    public String getTwitter() {
//        return twitter;
//    }
//
//    public void setTwitter(String twitter) {
//        this.twitter = twitter;
//    }
//
//    public String getLinkedin() {
//        return linkedin;
//    }
//
//    public void setLinkedin(String linkedin) {
//        this.linkedin = linkedin;
//    }
//
//    public String getKyc_method() {
//        return kyc_method;
//    }
//
//    public void setKyc_method(String kyc_method) {
//        this.kyc_method = kyc_method;
//    }
//
//    public String getCertificate_number() {
//        return certificate_number;
//    }
//
//    public void setCertificate_number(String certificate_number) {
//        this.certificate_number = certificate_number;
//    }
//
//    public String getCertificate_file_url() {
//        return certificate_file_url;
//    }
//
//    public void setCertificate_file_url(String certificate_file_url) {
//        this.certificate_file_url = certificate_file_url;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    // ✅ Add constructor, getters and setters
//    public CompanyProfileRequest(String company_name, String company_type, String company_size,
//                                 String email, String phone_number, String location, String nation_state,
//                                 String website, String twitter, String linkedin, String kyc_method,
//                                 String certificate_number, String certificate_file_url, String description) {
//        this.company_name = company_name;
//        this.company_type = company_type;
//        this.company_size = company_size;
//        this.email = email;
//        this.phone_number = phone_number;
//        this.location = location;
//        this.nation_state = nation_state;
//        this.website = website;
//        this.twitter = twitter;
//        this.linkedin = linkedin;
//        this.kyc_method = kyc_method;
//        this.certificate_number = certificate_number;
//        this.certificate_file_url = certificate_file_url;
//        this.description = description;
//    }
//}
//
public class CompanyProfileRequest {
    public String company_name;
    public String company_type;
    public String company_size;
    public String email;
    public String phone_number;
    public String location;
    public String nation_state;
    public String website;
    public String twitter;
    public String linkedin;
    public String kyc_method;
    public String certificate_number;
    public String certificate_file_url;
    public String description;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getCompany_size() {
        return company_size;
    }

    public void setCompany_size(String company_size) {
        this.company_size = company_size;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getNation_state() {
        return nation_state;
    }

    public void setNation_state(String nation_state) {
        this.nation_state = nation_state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getKyc_method() {
        return kyc_method;
    }

    public void setKyc_method(String kyc_method) {
        this.kyc_method = kyc_method;
    }

    public String getCertificate_number() {
        return certificate_number;
    }

    public void setCertificate_number(String certificate_number) {
        this.certificate_number = certificate_number;
    }

    public String getCertificate_file_url() {
        return certificate_file_url;
    }

    public void setCertificate_file_url(String certificate_file_url) {
        this.certificate_file_url = certificate_file_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompanyProfileRequest(String company_name, String company_type, String company_size,
                                 String email, String phone_number, String location, String nation_state,
                                 String website, String twitter, String linkedin,
                                 String kyc_method, String certificate_number,
                                 String certificate_file_url, String description) {
        this.company_name = company_name;
        this.company_type = company_type;
        this.company_size = company_size;
        this.email = email;
        this.phone_number = phone_number;
        this.location = location;
        this.nation_state = nation_state;
        this.website = website;
        this.twitter = twitter;
        this.linkedin = linkedin;
        this.kyc_method = kyc_method;
        this.certificate_number = certificate_number;
        this.certificate_file_url = certificate_file_url;
        this.description = description;
    }
}
