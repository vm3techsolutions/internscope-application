//package com.interns.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class CompanyProfile extends AppCompatActivity {
//
//    private ImageView companyLogo;
//    private TextView tvCompanyName, tvCompanyType, tvAboutCompany, tvCompanyEmail, tvCompanyPhone,
//            tvWebsite, tvCompanyAddress, tvCompanyState, tvCompanyLinkedIn, tvCompanyTwitter,
//            tvKycMethod, tvCertificateNumber, tvCertificateFileUrl;
//    private Button btnEditCompanyProfile;
//
//    private ApiService apiService;
//    private SessionManager sessionManager;
//    private static final String TAG = "CompanyProfileActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_company_profile);
//
//
//
//        apiService = ApiClient.getClient(this).create(ApiService.class);
//        sessionManager = new SessionManager(this);
//
//
//
//        // Initialize Views (make sure IDs match your XML)
//        companyLogo = findViewById(R.id.company_logo);
//        tvCompanyName = findViewById(R.id.tvCompanyName);
//        tvCompanyType = findViewById(R.id.tvCompanyType);
//        tvAboutCompany = findViewById(R.id.tvAboutCompany);
//        tvCompanyEmail = findViewById(R.id.tvCompanyEmail);
//        tvCompanyPhone = findViewById(R.id.tvCompanyPhone);
//        tvWebsite = findViewById(R.id.tvWebsite);
//        tvCompanyAddress = findViewById(R.id.tvCompanyAddress);
//        tvCompanyState = findViewById(R.id.tvCompanyState);
//        tvCompanyLinkedIn = findViewById(R.id.tvCompanyLinkedIn);
//        tvCompanyTwitter = findViewById(R.id.tvCompanyTwitter);
//        tvKycMethod = findViewById(R.id.tvKycMethod);
//        tvCertificateNumber = findViewById(R.id.tvCertificateNumber);
//        tvCertificateFileUrl = findViewById(R.id.tvCertificateFileUrl);
//        btnEditCompanyProfile = findViewById(R.id.btnEditCompanyProfile);
//
//        btnEditCompanyProfile.setOnClickListener(v -> {
//            Intent intent = new Intent(this, CompanyEditProfile.class);
//            startActivity(intent);
//        });
//
//        fetchCompanyProfile();
//    }
//
//    private void fetchCompanyProfile() {
//        String token = sessionManager.getCompanyToken();
//
//        Log.d("CompanyProfile", "Token being sent: " + token);
//        if (token == null) {
//            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Call<CompanyProfileResponse> call = apiService.getCompanyProfile();
//        call.enqueue(new Callback<CompanyProfileResponse>() {
//            @Override
//            public void onResponse(Call<CompanyProfileResponse> call, Response<CompanyProfileResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    CompanyProfileResponse profile = response.body();
//                    populateProfile(profile);
//                } else {
//                    Toast.makeText(CompanyProfile.this, "Failed to fetch profile", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Error code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CompanyProfileResponse> call, Throwable t) {
//                Toast.makeText(CompanyProfile.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                Log.e(TAG, "Failure: " + t.getMessage());
//            }
//        });
//    }
//
//    private void populateProfile(CompanyProfileResponse profile) {
//        // ✅ Company Info
//        tvCompanyName.setText(nonNull(profile.getCompanyName()));
//        tvCompanyType.setText(nonNull(profile.getCompanyType()) + " | " +
//                nonNull(profile.getCompanySize()) + " Employees");
//        tvAboutCompany.setText(nonNull(profile.getDescription()));
//
//        // ✅ Contact Info
//        tvCompanyEmail.setText("📧 " + nonNull(profile.getEmail()));
//        tvCompanyPhone.setText("📞 " + nonNull(profile.getPhoneNumber()));
//        tvWebsite.setText("🌐 " + nonNull(profile.getWebsite()));
//        tvCompanyAddress.setText("📍 " + nonNull(profile.getLocation()));
//        tvCompanyState.setText("🌏 " + nonNull(profile.getNationState()));
//        tvCompanyLinkedIn.setText("🔗 " + nonNull(profile.getLinkedin()));
//        tvCompanyTwitter.setText("🐦 " + nonNull(profile.getTwitter()));
//
//        // ✅ KYC Info
//        tvKycMethod.setText(nonNull(profile.getKycMethod()));
//        tvCertificateNumber.setText(nonNull(profile.getCertificateNumber()));
//        tvCertificateFileUrl.setText("📎 " + nonNull(profile.getCertificateFileUrl()));
//
//        // ✅ Load company logo or certificate image (optional)
//        if (profile.getCertificateFileUrl() != null && !profile.getCertificateFileUrl().isEmpty()) {
//            Glide.with(this)
//                    .load(profile.getCertificateFileUrl())
//                    .placeholder(R.drawable.user_94)
//                    .error(R.drawable.user_94)
//                    .into(companyLogo);
//        }
//    }
//
//    private String nonNull(String value) {
//        return value != null && !value.isEmpty() ? value : "N/A";
//    }
//}

package com.interns.internscopeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfile extends BaseActivity {

    private ImageView companyLogo;
    private TextView tvCompanyName, tvCompanyType, tvAboutCompany, tvCompanyEmail, tvCompanyPhone,
            tvWebsite, tvCompanyAddress, tvCompanyState, tvCompanyLinkedIn, tvCompanyTwitter,
            tvKycMethod, tvCertificateNumber, tvCertificateFileUrl;
    private Button btnEditCompanyProfile;

    private ApiService apiService;
    private static final String TAG = "CompanyProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        // Initialize Views (make sure IDs match your XML)
        companyLogo = findViewById(R.id.company_logo);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCompanyType = findViewById(R.id.tvCompanyType);
        tvAboutCompany = findViewById(R.id.tvAboutCompany);
        tvCompanyEmail = findViewById(R.id.tvCompanyEmail);
        tvCompanyPhone = findViewById(R.id.tvCompanyPhone);
        tvWebsite = findViewById(R.id.tvWebsite);
        tvCompanyAddress = findViewById(R.id.tvCompanyAddress);
        tvCompanyState = findViewById(R.id.tvCompanyState);
        tvCompanyLinkedIn = findViewById(R.id.tvCompanyLinkedIn);
        tvCompanyTwitter = findViewById(R.id.tvCompanyTwitter);
        tvKycMethod = findViewById(R.id.tvKycMethod);
        tvCertificateNumber = findViewById(R.id.tvCertificateNumber);
        tvCertificateFileUrl = findViewById(R.id.tvCertificateFileUrl);
        btnEditCompanyProfile = findViewById(R.id.btnEditCompanyProfile);

        btnEditCompanyProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, CompanyEditProfile.class);
            startActivity(intent);
        });

        setupDrawer();

        fetchCompanyProfile();
    }

    // ✅ Fetch profile — no manual token, handled by AuthInterceptor
    private void fetchCompanyProfile() {
        apiService.getCompanyProfile().enqueue(new Callback<CompanyProfileResponse>() {
            @Override
            public void onResponse(Call<CompanyProfileResponse> call, Response<CompanyProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CompanyProfileResponse profile = response.body();
                    populateProfile(profile);
                } else {
                    Toast.makeText(CompanyProfile.this, "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CompanyProfileResponse> call, Throwable t) {
                Toast.makeText(CompanyProfile.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Failure: " + t.getMessage());
            }
        });
    }

    private void populateProfile(CompanyProfileResponse profile) {
        // ✅ Company Info
        tvCompanyName.setText(nonNull(profile.getCompanyName()));
        tvCompanyType.setText(nonNull(profile.getCompanyType()) + " | " +
                nonNull(profile.getCompanySize()) + " Employees");
        tvAboutCompany.setText(nonNull(profile.getDescription()));

        // ✅ Contact Info
        tvCompanyEmail.setText("📧 " + nonNull(profile.getEmail()));
        tvCompanyPhone.setText("📞 " + nonNull(profile.getPhoneNumber()));
        tvWebsite.setText("🌐 " + nonNull(profile.getWebsite()));
        tvCompanyAddress.setText("📍 " + nonNull(profile.getLocation()));
        tvCompanyState.setText("🌏 " + nonNull(profile.getNationState()));
        tvCompanyLinkedIn.setText("🔗 " + nonNull(profile.getLinkedin()));
        tvCompanyTwitter.setText("🐦 " + nonNull(profile.getTwitter()));

        // ✅ KYC Info
//        tvKycMethod.setText(nonNull(profile.getKycMethod()));
//        tvCertificateNumber.setText(nonNull(profile.getCertificateNumber()));
//        tvCertificateFileUrl.setText("📎 " + nonNull(profile.getCertificateFileUrl()));

        // ✅ KYC Info
        tvKycMethod.setText(nonNull(profile.getKycMethod()));
        tvCertificateNumber.setText(nonNull(profile.getCertificateNumber()));

        String certUrl = profile.getCertificateFileUrl();
        tvCertificateFileUrl.setText("📎 " + nonNull(certUrl));

// ▶ Enable click to open document
        if (certUrl != null && !certUrl.trim().isEmpty()) {
            tvCertificateFileUrl.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(certUrl));
                startActivity(intent);
            });
        }


        // ✅ Load company logo or certificate image (optional)
        if (profile.getCertificateFileUrl() != null && !profile.getCertificateFileUrl().isEmpty()) {
            Glide.with(this)
                    .load(profile.getCertificateFileUrl())
                    .placeholder(R.drawable.user_94)
                    .error(R.drawable.user_94)
                    .into(companyLogo);
        }
    }

    private String nonNull(String value) {
        return value != null && !value.isEmpty() ? value : "N/A";
    }
}
