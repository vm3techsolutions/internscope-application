package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvPlanName, tvPlanPrice, tvPlanDesc, tvSubtotal, tvGst, tvTotal;
    private EditText etFullName, etEmail, etPhone;
    private Button btnConfirmPayment;

    private double subtotal = 0.0;
    private double gstAmount = 0.0;
    private double total = 0.0;
    private String planName, planDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views
        tvPlanName = findViewById(R.id.tvPlanName);
        tvPlanPrice = findViewById(R.id.tvPlanPrice);
        tvPlanDesc = findViewById(R.id.tvPlanDesc);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvGst = findViewById(R.id.tvGst);
        tvTotal = findViewById(R.id.tvTotal);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Receive data from Subscription Activity
        Intent intent = getIntent();
        if (intent != null) {
            planName = intent.getStringExtra("plan_name");
            planDesc = intent.getStringExtra("plan_description");
            subtotal = intent.getDoubleExtra("plan_price", 0.0);
        }

        // Set plan details
        tvPlanName.setText(planName != null ? planName : "Plan");
        tvPlanDesc.setText(planDesc != null ? planDesc : "");
        tvPlanPrice.setText("₹" + subtotal);

        // Calculate GST (18%)
        gstAmount = subtotal * 0.18;
        total = subtotal + gstAmount;

        tvSubtotal.setText("₹" + String.format("%.2f", subtotal));
        tvGst.setText("₹" + String.format("%.2f", gstAmount));
        tvTotal.setText("₹" + String.format("%.2f", total));

        // Confirm Payment Button Click
        btnConfirmPayment.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed to payment
            Intent paymentIntent = new Intent(CheckoutActivity.this, PaymentActivity.class);
            paymentIntent.putExtra("plan_name", planName);
            paymentIntent.putExtra("plan_price", total);
            paymentIntent.putExtra("user_name", name);
            paymentIntent.putExtra("user_email", email);
            paymentIntent.putExtra("user_phone", phone);
            startActivity(paymentIntent);
        });
    }
}
