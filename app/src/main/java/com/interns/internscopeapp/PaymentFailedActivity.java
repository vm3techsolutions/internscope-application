package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PaymentFailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);

        TextView tvError = findViewById(R.id.tvMessage);
        Button btnRetry = findViewById(R.id.btnRetry);

        String errorMessage = getIntent().getStringExtra("error_message");
        tvError.setText(errorMessage != null ? errorMessage : "Payment failed. Please try again.");

        btnRetry.setOnClickListener(v -> {
            // Redirect to Subscription again
            startActivity(new Intent(this, Subscription.class));
            finish();
        });
    }
}
