package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Company_login extends AppCompatActivity {

    Button userlogin;
    SessionManager sessionManager;
    EditText userEmail, userPass;
    TextView forgot_pass, signup;
    String userType = "company"; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        // Get user type from Intent
        userType = getIntent().getStringExtra("userType");
        if (userType == null) userType = "company"; // fallback

        // Initialize views
        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.userPass);
        userlogin = findViewById(R.id.loginbtn);
        forgot_pass = findViewById(R.id.forgot_pass);
        signup = findViewById(R.id.signup);
        ImageView togglePassword = findViewById(R.id.togglePasswordVisibility);

        final boolean[] isPasswordVisible = {false};

        togglePassword.setOnClickListener(v -> {
            if (isPasswordVisible[0]) {
                // Hide password
                userPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePassword.setImageResource(R.drawable.close_eye);
            } else {
                // Show password
                userPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePassword.setImageResource(R.drawable.view);
            }

            // Move cursor to end of text after toggling
            userPass.setSelection(userPass.getText().length());
            isPasswordVisible[0] = !isPasswordVisible[0];
        });

        // Initialize SessionManager
        sessionManager = new  SessionManager(this);

        // Change screen heading dynamically if needed (optional)
        setTitle(userType.equals("user") ? "Candidate Login" : "Company Login");

        // Login button click
        userlogin.setOnClickListener(v -> {
            String email = userEmail.getText().toString().trim();
            String password = userPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Company_login.this, "Please enter email & password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Forgot password click
        forgot_pass.setOnClickListener(v -> {
            startActivity(new Intent(Company_login.this, ForgotPass.class));
        });

        // Signup click
        signup.setOnClickListener(v -> {
            startActivity(new Intent(Company_login.this, CompanySignup.class));
        });
    }

    private void loginUser(String email, String password) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(email, password);

        // Dynamically choose API call based on user type
        Call<LoginResponse> call;
        if (userType.equals("company")) {
            call = apiService.loginCompany(loginRequest);
        } else {
            call = apiService.loginUser(loginRequest);
        }

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();

                    if (res.isSuccess() && res.getUser() != null) {
                        sessionManager.saveLoginSession(
                                res.getUser().getId(),
                                res.getUser().getUsername(),   // fullName
                                res.getUser().getUsername(),   // username
                                res.getUser().getEmail(),
                                res.getToken(),
                                userType
                        );

                        // ✅ Print token in Logcat
                        Log.d("LoginToken", "Received Token: " + res.getToken());

                        Toast.makeText(Company_login.this, userType + " Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Company_login.this, Home.class));
                        finish();
                    } else {
                        Toast.makeText(Company_login.this, "Login Failed: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Company_login.this, "Login Failed: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Company_login.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
