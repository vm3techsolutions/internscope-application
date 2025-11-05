package com.example.internscopeapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyEditProfile extends AppCompatActivity {

    private TextInputEditText etCompanyName, etEmail, etLocation, etState, etWebsite,
            etPhone, etLinkedIn, etCertificateNumber, etAboutCompany;
    private Spinner spCompanyType, spCompanySize;
    private RadioGroup rgKycMethod;
    private RadioButton rbGST, rbCOI, rbNone;
    private Button btnUploadCertificate, btnUpdate;
    private TextView tvSelectedFile;

    private Uri selectedFileUri;
    private SessionManager sessionManager;
    private ApiService apiService;

    private static final int FILE_PICKER_REQUEST_CODE = 101;
    private static final String TAG = "CompanyEditProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        initViews();

        btnUploadCertificate.setOnClickListener(v -> openFilePicker());
        btnUpdate.setOnClickListener(v -> {
            if (validateInputs()) uploadCompanyProfile();
        });
    }

    private void initViews() {
        etCompanyName = findViewById(R.id.etCompanyName);
        spCompanyType = findViewById(R.id.spCompanyType);
        spCompanySize = findViewById(R.id.spCompanySize);
        etEmail = findViewById(R.id.etEmail);
        etLocation = findViewById(R.id.etLocation);
        etState = findViewById(R.id.etState);
        etWebsite = findViewById(R.id.etWebsite);
        etPhone = findViewById(R.id.etPhone);
        etLinkedIn = findViewById(R.id.etLinkedIn);
        rgKycMethod = findViewById(R.id.rgKycMethod);
        rbGST = findViewById(R.id.rbGST);
        rbCOI = findViewById(R.id.rbCOI);
        rbNone = findViewById(R.id.rbNone);
        etCertificateNumber = findViewById(R.id.etCertificateNumber);
        etAboutCompany = findViewById(R.id.etAboutCompany);
        btnUploadCertificate = findViewById(R.id.btnUploadCertificate);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvSelectedFile = findViewById(R.id.tvSelectedFile);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select Certificate"), FILE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                tvSelectedFile.setText(selectedFileUri.getLastPathSegment());
            }
        }
    }

    private boolean validateInputs() {
        if (etCompanyName.getText().toString().trim().isEmpty()) {
            etCompanyName.setError("Required");
            return false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Required");
            return false;
        }
        if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Required");
            return false;
        }
        return true;
    }

    private void uploadCompanyProfile() {
        String token = sessionManager.getToken();

        if (token == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String companyName = etCompanyName.getText().toString().trim();
        String companyType = spCompanyType.getSelectedItem() != null ? spCompanyType.getSelectedItem().toString() : "";
        String companySize = spCompanySize.getSelectedItem() != null ? spCompanySize.getSelectedItem().toString() : "";
        String email = etEmail.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String website = etWebsite.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String linkedin = etLinkedIn.getText().toString().trim();
        String certificateNumber = etCertificateNumber.getText().toString().trim();
        String about = etAboutCompany.getText().toString().trim();

        int selectedKycId = rgKycMethod.getCheckedRadioButtonId();
        String kycMethod = "None";
        if (selectedKycId == rbGST.getId()) kycMethod = "GST Certificate";
        else if (selectedKycId == rbCOI.getId()) kycMethod = "Certificate of Incorporation";

        // Convert to RequestBody
        RequestBody companyNamePart = RequestBody.create(MediaType.parse("text/plain"), companyName);
        RequestBody companyTypePart = RequestBody.create(MediaType.parse("text/plain"), companyType);
        RequestBody companySizePart = RequestBody.create(MediaType.parse("text/plain"), companySize);
        RequestBody emailPart = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody phonePart = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody locationPart = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody statePart = RequestBody.create(MediaType.parse("text/plain"), state);
        RequestBody websitePart = RequestBody.create(MediaType.parse("text/plain"), website);
        RequestBody linkedinPart = RequestBody.create(MediaType.parse("text/plain"), linkedin);
        RequestBody kycPart = RequestBody.create(MediaType.parse("text/plain"), kycMethod);
        RequestBody certNoPart = RequestBody.create(MediaType.parse("text/plain"), certificateNumber);
        RequestBody aboutPart = RequestBody.create(MediaType.parse("text/plain"), about);

        MultipartBody.Part filePart = null;
//        if (selectedFileUri != null) {
//            String filePath = FileUtils.getPath(this, selectedFileUri);
//            if (filePath != null) {
//                File file = new File(filePath);
//                RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);
//                filePart = MultipartBody.Part.createFormData("certificate_file", file.getName(), requestFile);
//            }
//        }

        // Retrofit call (✅ no OkHttp client manually)
        Call<Void> call = apiService.updateCompanyProfile(
                "Bearer " + token,
                companyNamePart,
                companyTypePart,
                companySizePart,
                emailPart,
                phonePart,
                locationPart,
                statePart,
                websitePart,
                linkedinPart,
                kycPart,
                certNoPart,
                aboutPart,
                filePart
        );

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "Response code: " + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(CompanyEditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CompanyEditProfile.this, "Failed to update (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Upload failed", t);
                Toast.makeText(CompanyEditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
