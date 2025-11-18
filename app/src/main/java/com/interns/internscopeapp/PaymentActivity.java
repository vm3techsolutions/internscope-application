package com.interns.internscopeapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.math.BigDecimal;
import java.math.RoundingMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private static final String TAG = "PaymentActivity";

    private TextView tvPlanName, tvPlanDesc, tvSubtotal, tvGst, tvTotal;
    private ProgressBar progressBar;
    private Button payButton;

    private String planName, planDesc, userName, userEmail, userPhone, userType;
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal gstAmount;
    private static final BigDecimal GST_RATE = new BigDecimal("0.18");

    // ✅ ngrok base URL (no trailing slash)
    private static final String BASE_URL = "https://api.internscope.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvPlanName = findViewById(R.id.tvPlanName);
        tvPlanDesc = findViewById(R.id.tvPlanDesc);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvGst = findViewById(R.id.tvGst);
        tvTotal = findViewById(R.id.tvTotal);
        progressBar = findViewById(R.id.progressBar);
        payButton = findViewById(R.id.payButton);

        Intent i = getIntent();
        planName = i.getStringExtra("plan_name");
        planDesc = i.getStringExtra("plan_description");
        userName = i.getStringExtra("user_name");
        userEmail = i.getStringExtra("user_email");
        userPhone = i.getStringExtra("user_phone");
        userType = i.getStringExtra("user_type");

        double totalDouble = i.getDoubleExtra("plan_price", 0.0);
        total = BigDecimal.valueOf(totalDouble);

        BigDecimal divisor = BigDecimal.ONE.add(GST_RATE);
        subtotal = total.divide(divisor, 2, RoundingMode.HALF_UP);
        gstAmount = total.subtract(subtotal).setScale(2, RoundingMode.HALF_UP);

        tvPlanName.setText(planName != null ? planName : "Plan");
        tvPlanDesc.setText(planDesc != null ? planDesc : "");
        tvSubtotal.setText("₹" + subtotal.setScale(2, RoundingMode.HALF_UP).toPlainString());
        tvGst.setText("₹" + gstAmount.toPlainString());
        tvTotal.setText("₹" + total.setScale(2, RoundingMode.HALF_UP).toPlainString() + " (Including GST)");

        payButton.setOnClickListener(v -> initiatePayment());
    }

    private void initiatePayment() {
        progressBar.setVisibility(View.VISIBLE);
        payButton.setEnabled(false);

        SessionManager session = SessionManager.getInstance(this);
        String token = session.getActiveToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            payButton.setEnabled(true);
            return;
        }
        String authHeader = "Bearer " + token;

        PhonePePaymentRequest request = new PhonePePaymentRequest(
                total.doubleValue(),
                BASE_URL + "/api/payment/phonepe-callback",
                BASE_URL + "/api/payment/phonepe-callback"
        );

        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        Call<ApiRequestResponse> call;
        if ("company".equalsIgnoreCase(userType)) {
            call = api.initiateCompanyPayment(authHeader, request);
        } else {
            call = api.initiatePayment(authHeader, request);
        }

        call.enqueue(new Callback<ApiRequestResponse>() {
            @Override
            public void onResponse(Call<ApiRequestResponse> call, Response<ApiRequestResponse> response) {
                progressBar.setVisibility(View.GONE);
                payButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    String redirectUrl = response.body().getRedirectUrl();
                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        openPhonePeApp(redirectUrl);
                    } else {
                        Toast.makeText(PaymentActivity.this, "No redirect URL from server", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Missing redirectUrl in response: " + response.message());
                    }
                } else {
                    String err = "Initiate failed: " + response.code() + " " + response.message();
                    Toast.makeText(PaymentActivity.this, err, Toast.LENGTH_LONG).show();
                    Log.e(TAG, err);
                }
            }

            @Override
            public void onFailure(Call<ApiRequestResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                payButton.setEnabled(true);
                Toast.makeText(PaymentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "initiatePayment onFailure", t);
            }
        });
    }

    private void openPhonePeApp(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage("com.phonepe.app");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "PhonePe app not found, opening browser", e);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleCallbackIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleCallbackIntent(getIntent());
    }

    private void handleCallbackIntent(Intent intent) {
        if (intent == null) return;
        Uri data = intent.getData();
        if (data == null) return;

        String status = data.getQueryParameter("status");
        String txId = data.getQueryParameter("txId");
        String message = data.getQueryParameter("message");

        if (txId == null || txId.isEmpty()) {
            Log.d(TAG, "Callback without txId");
            return;
        }

        if ("success".equalsIgnoreCase(status)) {
            Toast.makeText(this, "Payment success. Verifying...", Toast.LENGTH_SHORT).show();
            verifyPaymentWithBackend(txId);
        } else {
            Toast.makeText(this, "Payment failed: " + message, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Payment failed callback: " + message);
        }
    }

//    private void verifyPaymentWithBackend(String transactionId) {
//        progressBar.setVisibility(View.VISIBLE);
//        payButton.setEnabled(false);
//
//        SessionManager session = new SessionManager(this);
//        String token = session.getToken();
//        String authHeader = "Bearer " + token;
//
//        ApiService api = ApiClient.getClient(this).create(ApiService.class);
//        Call<PaymentStatusResponse> call;
//        if ("company".equalsIgnoreCase(userType)) {
//            call = api.verifyCompanyPayment(authHeader, transactionId);
//        } else {
//            call = api.verifyUserPayment(authHeader, transactionId);
//        }
//
//        call.enqueue(new Callback<PaymentStatusResponse>() {
//            @Override
//            public void onResponse(Call<PaymentStatusResponse> call, Response<PaymentStatusResponse> response) {
//                progressBar.setVisibility(View.GONE);
//                payButton.setEnabled(true);
//
//                if (response.isSuccessful() && response.body() != null) {
//                    boolean ok = response.body().isSuccess();
//                    if (ok) {
//                        Toast.makeText(PaymentActivity.this, "Transaction confirmed", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(PaymentActivity.this, Home.class));
//                        finish();
//                    } else {
//                        Toast.makeText(PaymentActivity.this, "Verification failed: " + response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(PaymentActivity.this, "Verify API error: " + response.code(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PaymentStatusResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                payButton.setEnabled(true);
//                Toast.makeText(PaymentActivity.this, "Verify network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
private void verifyPaymentWithBackend(String transactionId) {
    progressBar.setVisibility(View.VISIBLE);
    payButton.setEnabled(false);

    SessionManager session = new SessionManager(this);
    String token = session.getActiveToken();
    String authHeader = "Bearer " + token;

    ApiService api = ApiClient.getClient(this).create(ApiService.class);
    Call<PaymentStatusResponse> call;
    if ("company".equalsIgnoreCase(userType)) {
        call = api.verifyCompanyPayment(authHeader, transactionId);
    } else {
        call = api.verifyUserPayment(authHeader, transactionId);
    }

    call.enqueue(new Callback<PaymentStatusResponse>() {
        @Override
        public void onResponse(Call<PaymentStatusResponse> call, Response<PaymentStatusResponse> response) {
            progressBar.setVisibility(View.GONE);
            payButton.setEnabled(true);

            if (response.isSuccessful() && response.body() != null) {
                PaymentStatusResponse result = response.body();
                if (result.isSuccess()) {
                    Toast.makeText(PaymentActivity.this,
                            "Payment verified successfully!\nInvoice sent to your email.",
                            Toast.LENGTH_LONG).show();

                    Intent successIntent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
                    successIntent.putExtra("invoice_status", "sent");
                    successIntent.putExtra("user_email", userEmail);
                    startActivity(successIntent);
                    finish();
                } else {
                    Toast.makeText(PaymentActivity.this,
                            "Verification failed: " + result.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(PaymentActivity.this,
                        "Server error during verification: " + response.code(),
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<PaymentStatusResponse> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            payButton.setEnabled(true);
            Toast.makeText(PaymentActivity.this,
                    "Network error while verifying payment: " + t.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    });
}

}
