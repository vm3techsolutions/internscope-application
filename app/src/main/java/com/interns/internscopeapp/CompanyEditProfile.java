package com.interns.internscopeapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Toast;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    private ApiService apiService;

    private static final int FILE_PICKER_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        initViews();
        loadCompanyProfile();

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
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), FILE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                tvSelectedFile.setText(getFileName(selectedFileUri));
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
        if (selectedFileUri != null) {
            requestSignedUrlForFile();
        } else {
            sendProfileData("");
        }
    }

    private void requestSignedUrlForFile() {
        String fileName = getFileName(selectedFileUri);
        String mimeType = getContentResolver().getType(selectedFileUri);

        FileRequestBody fileBody = new FileRequestBody(fileName, mimeType);

        apiService.getSignedUrl(fileBody).enqueue(new Callback<SignedUrlResponse>() {
            @Override
            public void onResponse(Call<SignedUrlResponse> call, Response<SignedUrlResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String signedUrl = response.body().getSignedUrl();
                    String s3Key = response.body().getS3Key();   // ← use s3Key

                    uploadFileToS3(signedUrl, s3Key, mimeType);
                    //uploadFileToS3(response.body().getSignedUrl(), response.body().getS3Key(), mimeType);
                } else {
                    Toast.makeText(CompanyEditProfile.this, "Failed to get upload URL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignedUrlResponse> call, Throwable t) {
                Toast.makeText(CompanyEditProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFileToS3(String signedUrl, String s3Key, String mimeType) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
//            byte[] fileBytes = inputStream.readAllBytes();
//            inputStream.close();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
            }

            byte[] fileBytes = buffer.toByteArray();
            inputStream.close();


            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), fileBytes);

            Request request = new Request.Builder()
                    .url(signedUrl)
                    .put(requestBody)
                    .build();

            new Thread(() -> {
                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(this, "Document uploaded!", Toast.LENGTH_SHORT).show();
                            sendProfileData(s3Key);
                        } else {
                            Toast.makeText(this, "Upload failed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();

        } catch (Exception e) {
            Toast.makeText(this, "File reading error", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendProfileData(String uploadedFileUrl) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            jsonObject.put("company_name", etCompanyName.getText().toString().trim());
            jsonObject.put("company_type", spCompanyType.getSelectedItem().toString());
            jsonObject.put("company_size", spCompanySize.getSelectedItem().toString());
            jsonObject.put("email", etEmail.getText().toString().trim());
            jsonObject.put("phone_number", etPhone.getText().toString().trim());
            jsonObject.put("location", etLocation.getText().toString().trim());
            jsonObject.put("nation_state", etState.getText().toString().trim());
            jsonObject.put("website", etWebsite.getText().toString().trim());
            jsonObject.put("linkedin", etLinkedIn.getText().toString().trim());
            jsonObject.put("certificate_number", etCertificateNumber.getText().toString().trim());
            jsonObject.put("description", etAboutCompany.getText().toString().trim());

            int selectedKyc = rgKycMethod.getCheckedRadioButtonId();
            String kycMethod = "None";
            if (selectedKyc == rbGST.getId()) kycMethod = "GST Certificate";
            else if (selectedKyc == rbCOI.getId()) kycMethod = "Certificate of Incorporation";
            jsonObject.put("kyc_method", kycMethod);

            jsonObject.put("certificate_file_url", uploadedFileUrl);

            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

            apiService.updateCompanyProfile(requestBody).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CompanyEditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CompanyEditProfile.this, "Update failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CompanyEditProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error preparing data", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return "file";
        int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(index);
        cursor.close();
        return name;
    }

    private void loadCompanyProfile() {
        apiService.getCompanyProfile().enqueue(new Callback<CompanyProfileResponse>() {
            @Override
            public void onResponse(Call<CompanyProfileResponse> call, Response<CompanyProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CompanyProfileResponse cp = response.body();
                    etCompanyName.setText(cp.getCompanyName());
                    etEmail.setText(cp.getEmail());
                    etLocation.setText(cp.getLocation());
                    etState.setText(cp.getNationState());
                    etWebsite.setText(cp.getWebsite());
                    etPhone.setText(cp.getPhoneNumber());
                    etLinkedIn.setText(cp.getLinkedin());
                    etCertificateNumber.setText(cp.getCertificateNumber());
                    etAboutCompany.setText(cp.getDescription());
                } else {
                    Toast.makeText(CompanyEditProfile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyProfileResponse> call, Throwable t) {
                Toast.makeText(CompanyEditProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
