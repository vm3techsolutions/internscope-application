package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verify extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnResetPassword;
    private TextView tvBackToLogin;

    private ApiService apiService;
    private SessionManager sessionManager;
    private String role;  // "student" or "company"
    private String token; // received via intent or deep link

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        apiService = ApiClient.getClient(this).create(ApiService.class);
        sessionManager = SessionManager.getInstance(this);
        role = sessionManager.getUserType();

        // Get token from intent if sent from email or previous activity
        token = getIntent().getStringExtra("token");
        Log.d("ResetPassword", "Received token: " + token);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Verify.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(Verify.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (token == null || token.isEmpty()) {
                    Toast.makeText(Verify.this, "Invalid or missing token", Toast.LENGTH_SHORT).show();
                    return;
                }

                resetPassword(token, newPassword);
            }
        });


        tvBackToLogin.setOnClickListener(v -> {
            String userType = sessionManager.getUserType(); // "student" or "company"

            if ("company".equalsIgnoreCase(userType)) {
                Intent intent = new Intent(Verify.this, Company_login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Verify.this, Login.class);
                startActivity(intent);
            }

            finish();
        });
    }

    private void resetPassword(String token, String newPassword) {
        Call<ApiResponse> call;

        if ("company".equalsIgnoreCase(role)) {
            call = apiService.resetCompanyPassword(token, newPassword);
        } else {
            call = apiService.resetUserPassword(token, newPassword);
        }

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Verify.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("ResetPassword", "Response: " + response.body().getMessage());
                    finish();
                } else {
                    Toast.makeText(Verify.this, "Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("ResetPassword", "Response failed: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Verify.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ResetPassword", "Error: " + t.getMessage(), t);
            }
        });
    }
}
