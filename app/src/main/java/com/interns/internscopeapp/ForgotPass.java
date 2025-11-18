package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPass extends AppCompatActivity {

    private EditText emailInput;
    private Button sendButton;
    private ApiService apiService;
    private SessionManager sessionManager;
    private String role; // "student" or "company"

    private Button backLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass); // your XML layout

        emailInput = findViewById(R.id.emailInput);
        sendButton = findViewById(R.id.codesend);
        backLogin = findViewById(R.id.backLogin);

        apiService = ApiClient.getClient(this).create(ApiService.class);
        sessionManager = SessionManager.getInstance(this);

        // Get role from session manager
        role = sessionManager.getUserType(); // returns "student" or "company"
        Log.d("ForgotPassword", "Detected role: " + role);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPass.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendForgotPasswordRequest(email);
            }
        });

        backLogin.setOnClickListener(v -> {
            String userType = sessionManager.getUserType(); // "student" or "company"

            if ("company".equalsIgnoreCase(userType)) {
                Intent intent = new Intent(ForgotPass.this, Company_login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ForgotPass.this, Login.class);
                startActivity(intent);
            }

            finish();
        });

    }

    private void sendForgotPasswordRequest(String email) {
        Call<ApiResponse> call;

        // Choose correct API endpoint based on role
        if ("company".equalsIgnoreCase(role)) {
            call = apiService.forgotCompanyPassword(email);
        } else {
            call = apiService.forgotUserPassword(email);
        }

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ForgotPass.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    // Move to Reset Password screen
//                    Intent intent = new Intent(ForgotPass.this, Verify.class);
//                    startActivity(intent);
                } else {
                    Toast.makeText(ForgotPass.this, "Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ForgotPassword", "Error: " + t.getMessage());
                Toast.makeText(ForgotPass.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
