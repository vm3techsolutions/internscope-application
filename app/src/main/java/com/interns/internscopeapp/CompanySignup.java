package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanySignup extends AppCompatActivity {

    EditText fname, lname, username, email, password, companyName;
    Spinner companyTypeSpinner;
    Button registerBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_signup);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        username = findViewById(R.id.username);
        email = findViewById(R.id.CompanyEmail);
        password = findViewById(R.id.CompanyPass);
        companyName = findViewById(R.id.companyName);
        companyTypeSpinner = findViewById(R.id.companyType);
        registerBtn = findViewById(R.id.CompanysignUpbtn);
        loginBtn = findViewById(R.id.Companyloginbtn);

        // Populate spinner with company type options
        String[] companyTypes = {
                "Information Technology (IT) / Software",
                "Finance / Banking",
                "Manufacturing",
                "Healthcare",
                "Education",
                "Construction / Engineering",
                "Media / Advertising",
                "Retail / E-commerce",
                "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, companyTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companyTypeSpinner.setAdapter(adapter);

        registerBtn.setOnClickListener(v -> registerCompany());
        loginBtn.setOnClickListener(v -> startActivity(new Intent(this, Company_login.class)));
    }

    private void registerCompany() {
        String firstName = fname.getText().toString().trim();
        String lastName = lname.getText().toString().trim();
        String uname = username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String compName = companyName.getText().toString().trim();
        String compType = companyTypeSpinner.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || uname.isEmpty() || mail.isEmpty() ||
                pass.isEmpty() || compName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        CompanySignupRequest request = new CompanySignupRequest(firstName, lastName, uname, mail, pass, compName, compType);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.registerCompany(request);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse res = response.body();
                    if (res.isSuccess()) {
                        Toast.makeText(CompanySignup.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CompanySignup.this, Company_login.class));
                        finish();
                    } else {
                        Toast.makeText(CompanySignup.this, "Failed: " + res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CompanySignup.this, "Invalid response: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(CompanySignup.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
