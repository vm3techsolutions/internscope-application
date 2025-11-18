package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        String planName = getIntent().getStringExtra("plan_name");
        String paymentId = getIntent().getStringExtra("payment_id");

        TextView tvMessage = findViewById(R.id.tvSuccessMessage);
        Button btnGoHome = findViewById(R.id.btnGoHome);

        tvMessage.setText("🎉 Payment Successful!\n\nPlan: " + planName + "\nPayment ID: " + paymentId);

        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, Profile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
