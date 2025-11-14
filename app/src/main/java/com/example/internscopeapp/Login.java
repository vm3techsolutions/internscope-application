//package com.example.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class Login extends AppCompatActivity {
//
//    Button userlogin;
//    SessionManager sessionManager;
//    EditText userEmail, userPass;
//    TextView forgot_pass, signup;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        // Initialize views
//        userEmail = findViewById(R.id.userEmail);
//        userPass = findViewById(R.id.userPass);
//        userlogin = findViewById(R.id.loginbtn);
//        forgot_pass = findViewById(R.id.forgot_pass);
//        signup = findViewById(R.id.signup);
//
//        // Initialize SessionManager
//        sessionManager = new SessionManager(this);
//
//        // Login button click
//        userlogin.setOnClickListener(v -> {
//            String email = userEmail.getText().toString().trim();
//            String password = userPass.getText().toString().trim();
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(Login.this, "Please enter email & password", Toast.LENGTH_SHORT).show();
//            } else {
//                loginUser(email, password);
//            }
//        });
//
//        // Forgot password click
//        forgot_pass.setOnClickListener(v -> {
//            startActivity(new Intent(Login.this, forgotPass.class));
//        });
//
//        // Signup click
//        signup.setOnClickListener(v -> {
//            startActivity(new Intent(Login.this, SignUp.class));
//        });
//    }
//
//    private void loginUser(String email, String password) {
//        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//
//        LoginRequest loginRequest = new LoginRequest(email, password);
//        Call<LoginResponse> call = apiService.loginUser(loginRequest);
//
//        call.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    LoginResponse res = response.body();
//
//                    if (res.isSuccess() && res.getUser() != null) {
//                        sessionManager.saveLoginSession(
//                                res.getUser().getId(),
//                                res.getUser().getUsername(),
//                                res.getUser().getUsername(),
//                                res.getUser().getEmail(),
//                                res.getToken()
//                        );
//
//                        Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(Login.this, Home.class));
//                        finish();
//                    } else {
//                        Toast.makeText(Login.this, "Login Failed: " + res.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(Login.this, "Login Failed: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                t.printStackTrace();
//            }
//        });
//    }
//}

package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    Button userlogin;
    SessionManager sessionManager;
    EditText userEmail, userPass;
    TextView forgot_pass, signup;
    String userType = "user"; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get user type from Intent
        userType = getIntent().getStringExtra("userType");
        if (userType == null) userType = "user"; // fallback

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
       // setTitle(userType.equals("company") ? "Company Login" : "Candidate Login");

        // Login button click
        userlogin.setOnClickListener(v -> {
            String email = userEmail.getText().toString().trim();
            String password = userPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Please enter email & password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Forgot password click
        forgot_pass.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, ForgotPass.class));
        });

        // Signup click
        signup.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, SignUp.class));
        });
    }

    private void loginUser(String email, String password) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<LoginResponse> call = null;

        LoginRequest loginRequest = new LoginRequest(email, password);

        // Dynamically choose API call based on user type


        if (userType.equals("company")) {
            call = apiService.loginCompany(loginRequest);

        } else {
            call = apiService.loginUser(loginRequest);
        }

        Log.d("API_URL", call.request().url().toString());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();

                    if (res.isSuccess() && res.getUser() != null) {
                        sessionManager.saveLoginSession(
                                res.getUser().getId(),
                                res.getUser().getUsername(),   // fullName (you can use username as fullName if not available)
                                res.getUser().getUsername(),   // username
                                res.getUser().getEmail(),
                                res.getToken(),
                                userType
                        );

                        Toast.makeText(Login.this, userType + " Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Home.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Login Failed: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Login Failed: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
