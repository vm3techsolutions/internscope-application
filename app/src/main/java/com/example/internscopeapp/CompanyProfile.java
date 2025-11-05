package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfile extends AppCompatActivity {

    private ImageView companyLogo;
    private TextView tvCompanyName, tvIndustry, tvWebsite, tvEmail, tvPhone,
            tvDescription, tvAddress, tvCity, tvState, tvCountry;
    private Button btnEditCompanyProfile;

    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        //sessionManager = new SessionManager(this);
       // apiService = ApiClient.getClient(this).create(ApiService.class);

//        // ✅ Initialize Views
//        companyLogo = findViewById(R.id.company_logo);
//        tvCompanyName = findViewById(R.id.tvCompanyName);
//        tvIndustry = findViewById(R.id.tvIndustry);
//        tvWebsite = findViewById(R.id.tvWebsite);
//        tvEmail = findViewById(R.id.tvEmail);
//        tvPhone = findViewById(R.id.tvPhone);
//        tvDescription = findViewById(R.id.tvDescription);
//        tvAddress = findViewById(R.id.tvCompanyAddress);
//        tvState = findViewById(R.id.tvCompanyState);

        btnEditCompanyProfile = findViewById(R.id.btnEditCompanyProfile);

//        companyLogo.setImageResource(R.drawable.user_94); // default logo
//
//        String token = sessionManager.getToken();
//        int companyId = sessionManager.getUserId(); // assuming companyId stored as userId
//
//        if (token != null && !token.isEmpty() && companyId > 0) {
//            fetchCompanyProfile(token, companyId);
//        } else {
//            Toast.makeText(this, "Please log in again.", Toast.LENGTH_SHORT).show();
//        }

        // ✅ Redirect to CompanyEditProfile
        btnEditCompanyProfile.setOnClickListener(v -> {
            Intent intent = new Intent(CompanyProfile.this, CompanyEditProfile.class);
            startActivity(intent);
        });
    }
}

    // ✅ Fetch company info dynamically from backend
//    private void fetchCompanyProfile(String token, int companyId) {
//        apiService.getCompanyProfile("Bearer " + token, companyId).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    JsonObject company = response.body();
//
//                    tvCompanyName.setText(safeText(company.get("company_name").getAsString()));
//                    tvIndustry.setText(safeText(company.get("industry").getAsString()));
//                    tvWebsite.setText(safeText(company.get("website").getAsString()));
//                    tvEmail.setText(safeText(company.get("email").getAsString()));
//                    tvPhone.setText(safeText(company.get("phone").getAsString()));
//                    tvDescription.setText(safeText(company.get("description").getAsString()));
//                    tvAddress.setText(safeText(company.get("address").getAsString()));
//                    tvCity.setText(safeText(company.get("city").getAsString()));
//                    tvState.setText(safeText(company.get("state").getAsString()));
//                    tvCountry.setText(safeText(company.get("country").getAsString()));
//
//                } else {
//                    Toast.makeText(CompanyProfile.this, "Failed to load company profile", Toast.LENGTH_SHORT).show();
//                    Log.e("CompanyProfile", "Response code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(CompanyProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // ✅ Safe text helper
//    private String safeText(String text) {
//        return (text != null && !text.trim().isEmpty()) ? text : "N/A";
//    }
//}
