package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    EditText fname, lname, username, userEmail, userPass;
    Button signUpbtn, loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        username = findViewById(R.id.username);
        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.userPass);
        signUpbtn = findViewById(R.id.signUpbtn);
        loginbtn = findViewById(R.id.loginbtn); // NEW

        // Sign Up button click
        signUpbtn.setOnClickListener(view -> {
            String firstName = fname.getText().toString().trim();
            String lastName = lname.getText().toString().trim();
            String uname = username.getText().toString().trim();
            String email = userEmail.getText().toString().trim();
            String password = userPass.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || uname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUp.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(firstName, lastName, uname, email, password);
            }
        });

        // Login button click → open Login activity
        loginbtn.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, Login.class));
        });
    }

    private void registerUser(String firstName, String lastName, String username, String email, String password) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        SignupRequest signupRequest = new SignupRequest(firstName, lastName, username, email, password);

        Call<UserResponse> call = apiService.registerUser(signupRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse res = response.body();
                    if (res.isSuccess()) {
                        Toast.makeText(SignUp.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "Failed: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errMsg = "HTTP " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SignUp.this, "Registration Failed: " + errMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(SignUp.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
