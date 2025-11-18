//package com.interns.internscopeapp;
//
//public class ApiRequestResponse {
//    private String redirectUrl;
//    private String error;
//    private boolean success;
//    private String code;
//    private String message;
//
//    public String getRedirectUrl() {
//        return redirectUrl;
//    }
//
//    public void setRedirectUrl(String redirectUrl) {
//        this.redirectUrl = redirectUrl;
//    }
//
//    public String getError() {
//        return error;
//    }
//
//    public void setError(String error) {
//        this.error = error;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//}

package com.interns.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class ApiRequestResponse {

    @SerializedName("redirectUrl")
    private String redirectUrl;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
