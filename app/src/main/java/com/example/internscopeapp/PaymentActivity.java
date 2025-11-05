package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private String planName, userName, userEmail, userPhone;
    private double planPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Checkout.preload(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            planName = intent.getStringExtra("plan_name");
            planPrice = intent.getDoubleExtra("plan_price", 0.0);
            userName = intent.getStringExtra("user_name");
            userEmail = intent.getStringExtra("user_email");
            userPhone = intent.getStringExtra("user_phone");
        }

        startRazorpayPayment();
    }

    private void startRazorpayPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_RKzGJ4V90ygiji"); // Replace with your real key
        checkout.setFullScreenDisable(true);

        try {
            // Disable SMS retriever leak (safe reflection)
            java.lang.reflect.Method method = Checkout.class.getMethod("setDisableSmsRetriever", boolean.class);
            method.invoke(checkout, true);
        } catch (Exception ignored) {}

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Internscope");
            options.put("description", planName);
            options.put("currency", "INR");
            options.put("amount", Math.round(planPrice * 100)); // Convert to paisa

            JSONObject prefill = new JSONObject();
            prefill.put("email", userEmail);
            prefill.put("contact", userPhone);
            options.put("prefill", prefill);

            checkout.open(PaymentActivity.this, options);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Payment initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
        intent.putExtra("payment_id", razorpayPaymentID);
        intent.putExtra("plan_name", planName);
        intent.putExtra("amount", planPrice);

        // ✅ This prevents back navigation or blank screen issue
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finishAffinity(); // ✅ Closes all parent activities
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed: " + response, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(PaymentActivity.this, PaymentFailedActivity.class);
        intent.putExtra("error_message", response);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Checkout.clearUserData(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
