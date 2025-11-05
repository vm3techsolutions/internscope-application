package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentStatusActivity extends AppCompatActivity {

    private ImageView statusIcon;
    private TextView statusText, amountText, transactionIdText;
    private Button btnGoHome;

    private String paymentId;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        // Initialize views
        statusIcon = findViewById(R.id.statusIcon);
        statusText = findViewById(R.id.statusText);
        amountText = findViewById(R.id.amountText);
        transactionIdText = findViewById(R.id.transactionIdText);
        btnGoHome = findViewById(R.id.btnGoHome);

        // Get data from intent
        paymentId = getIntent().getStringExtra("payment_id");
        amount = getIntent().getDoubleExtra("amount", 0.0);

        // Display amount initially
        amountText.setText("₹ " + String.format("%.2f", amount));

        // Verify payment from backend
        verifyPhonePePayment(paymentId);

        btnGoHome.setOnClickListener(v -> {
            // Redirect to Dashboard or Home
            Intent intent = new Intent(PaymentStatusActivity.this, Profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void verifyPhonePePayment(String paymentId) {
        JsonObject body = new JsonObject();
        body.addProperty("payment_id", paymentId);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<JsonObject> call = apiService.verifyPhonePePayment(body);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject res = response.body();
                    String status = res.has("status") ? res.get("status").getAsString() : "failed";

                    if (status.equalsIgnoreCase("success")) {
                        showPaymentSuccess(res);
                    } else {
                        showPaymentFailed();
                    }
                } else {
                    showPaymentFailed();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PaymentStatusActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                showPaymentFailed();
            }
        });
    }

    private void showPaymentSuccess(JsonObject res) {
        statusIcon.setImageResource(R.drawable.check); // ✅ replace with green tick icon
        statusText.setText("Payment Successful");
        statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        String txnId = res.has("payment_id") ? res.get("payment_id").getAsString() : paymentId;
        transactionIdText.setText("Transaction ID: " + txnId);

        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
    }

    private void showPaymentFailed() {
        statusIcon.setImageResource(android.R.drawable.btn_dialog); // ✅ replace with red cross icon
        statusText.setText("Payment Failed");
        statusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        transactionIdText.setText("Transaction ID: " + paymentId);
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}
