//package com.example.internscopeapp;
//
//public class PhonePePaymentRequest {
//    private double amount;
//
//    public PhonePePaymentRequest(double amount) {
//        this.amount = amount;
//    }
//
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//}

//package com.example.internscopeapp;
//
//import com.google.gson.annotations.SerializedName;
//
//public class PhonePePaymentRequest {
//
//    @SerializedName("amount")
//    private double amount;
//
//    @SerializedName("callbackUrl")
//    private String callbackUrl;

    // Optional: You can add other fields like "orderId", "customerId" if your backend requires
    // @SerializedName("orderId")
    // private String orderId;

//    public PhonePePaymentRequest(double amount) {
//        this.amount = amount;
//        this.callbackUrl = "internscopeapp://paymentcallback"; // default callback URL
//    }
//
//    public PhonePePaymentRequest(double amount, String callbackUrl) {
//        this.amount = amount;
//        this.callbackUrl = callbackUrl;
//    }
//
//    // Getters and setters
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public String getCallbackUrl() {
//        return callbackUrl;
//    }
//
//    public void setCallbackUrl(String callbackUrl) {
//        this.callbackUrl = callbackUrl;
//    }
//}

//package com.example.internscopeapp;
//
//import com.google.gson.annotations.SerializedName;
//
//    public class PhonePePaymentRequest {
//
//        @SerializedName("amount")
//        private double amount;
//
//        @SerializedName("callbackUrl")
//        private String callbackUrl;
//
//        @SerializedName("redirectUrl")
//        private String redirectUrl;
//
//        public PhonePePaymentRequest(double amount, String callbackUrl, String redirectUrl) {
//            this.amount = amount;
//            this.callbackUrl = callbackUrl;
//            this.redirectUrl = redirectUrl;
//        }
//
//        public double getAmount() {
//            return amount;
//        }
//
//        public void setAmount(double amount) {
//            this.amount = amount;
//        }
//
//        public String getCallbackUrl() {
//            return callbackUrl;
//        }
//
//        public void setCallbackUrl(String callbackUrl) {
//            this.callbackUrl = callbackUrl;
//        }
//
//        public String getRedirectUrl() {
//            return redirectUrl;
//        }
//
//        public void setRedirectUrl(String redirectUrl) {
//            this.redirectUrl = redirectUrl;
//        }
//    }
//

//package com.example.internscopeapp;
//
//import com.google.gson.annotations.SerializedName;
//
///**
// * Request object to send payment details to backend for PhonePe integration.
// */
//public class PhonePePaymentRequest {
//
//    @SerializedName("amount")
//    private double amount;
//
//    @SerializedName("name")
//    private String name;
//
//    @SerializedName("email")
//    private String email;
//
//    @SerializedName("phone")
//    private String phone;
//
//    @SerializedName("plan")
//    private String plan;
//
//    public PhonePePaymentRequest(double amount, String name, String email, String phone, String plan) {
//        this.amount = amount;
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//        this.plan = plan;
//    }
//
//    // Getter and Setter methods
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
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
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getPlan() {
//        return plan;
//    }
//
//    public void setPlan(String plan) {
//        this.plan = plan;
//    }
//}

package com.example.internscopeapp;

public class PhonePePaymentRequest {
    private double amount;
    private String callbackUrl;
    private String redirectUrl;

    public PhonePePaymentRequest(double amount, String callbackUrl, String redirectUrl) {
        this.amount = amount;
        this.callbackUrl = callbackUrl;
        this.redirectUrl = redirectUrl;
    }

    // Getters
    public double getAmount() {
        return amount;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    // Setters (optional)
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
