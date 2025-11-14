//package com.example.internscopeapp;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.*;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.textfield.TextInputEditText;
//
//import java.io.File;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class CompanyEditProfile extends AppCompatActivity {
//
//    private TextInputEditText etCompanyName, etEmail, etLocation, etState, etWebsite,
//            etPhone, etLinkedIn, etCertificateNumber, etAboutCompany;
//    private Spinner spCompanyType, spCompanySize;
//    private RadioGroup rgKycMethod;
//    private RadioButton rbGST, rbCOI, rbNone;
//    private Button btnUploadCertificate, btnUpdate;
//    private TextView tvSelectedFile;
//
//    private Uri selectedFileUri;
//    private SessionManager sessionManager;
//    private ApiService apiService;
//
//    private static final int FILE_PICKER_REQUEST_CODE = 101;
//    private static final String TAG = "CompanyEditProfile";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_company_edit_profile);
//
//        sessionManager = new SessionManager(this);
//        apiService = ApiClient.getClient(this).create(ApiService.class);
//
//        initViews();
//        loadCompanyProfile();
//
//        btnUploadCertificate.setOnClickListener(v -> openFilePicker());
//        btnUpdate.setOnClickListener(v -> {
//            if (validateInputs()) uploadCompanyProfile();
//        });
//    }
//
//    private void initViews() {
//        etCompanyName = findViewById(R.id.etCompanyName);
//        spCompanyType = findViewById(R.id.spCompanyType);
//        spCompanySize = findViewById(R.id.spCompanySize);
//        etEmail = findViewById(R.id.etEmail);
//        etLocation = findViewById(R.id.etLocation);
//        etState = findViewById(R.id.etState);
//        etWebsite = findViewById(R.id.etWebsite);
//        etPhone = findViewById(R.id.etPhone);
//        etLinkedIn = findViewById(R.id.etLinkedIn);
//        rgKycMethod = findViewById(R.id.rgKycMethod);
//        rbGST = findViewById(R.id.rbGST);
//        rbCOI = findViewById(R.id.rbCOI);
//        rbNone = findViewById(R.id.rbNone);
//        etCertificateNumber = findViewById(R.id.etCertificateNumber);
//        etAboutCompany = findViewById(R.id.etAboutCompany);
//        btnUploadCertificate = findViewById(R.id.btnUploadCertificate);
//        btnUpdate = findViewById(R.id.btnUpdate);
//        tvSelectedFile = findViewById(R.id.tvSelectedFile);
//    }
//
//    private void openFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        startActivityForResult(Intent.createChooser(intent, "Select Certificate"), FILE_PICKER_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            selectedFileUri = data.getData();
//            if (selectedFileUri != null) {
//                tvSelectedFile.setText(selectedFileUri.getLastPathSegment());
//            }
//        }
//    }
//
//    private boolean validateInputs() {
//        if (etCompanyName.getText().toString().trim().isEmpty()) {
//            etCompanyName.setError("Required");
//            return false;
//        }
//        if (etEmail.getText().toString().trim().isEmpty()) {
//            etEmail.setError("Required");
//            return false;
//        }
//        if (etPhone.getText().toString().trim().isEmpty()) {
//            etPhone.setError("Required");
//            return false;
//        }
//        return true;
//    }
//
//    private void uploadCompanyProfile() {
//        String token = sessionManager.getToken();
//        if (token == null) {
//            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (selectedFileUri != null) {
//            uploadFileToS3(token); // Upload certificate first
//        } else {
//            sendProfileData(token, null); // No file selected
//        }
//    }
//
//    private void uploadFileToS3(String token) {
//        String filePath = FileUtils.getPath(this, selectedFileUri);
//        if (filePath == null) {
//            Toast.makeText(this, "Invalid file path", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        File file = new File(filePath);
//        String mimeType = getContentResolver().getType(selectedFileUri);
//
//        FileRequestBody fileBody = new FileRequestBody(file.getName(), mimeType);
//        apiService.getSignedUrl("Bearer " + token, fileBody).enqueue(new Callback<SignedUrlResponse>() {
//            @Override
//            public void onResponse(Call<SignedUrlResponse> call, Response<SignedUrlResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    String signedUrl = response.body().getSignedUrl();
//                    String fileUrl = response.body().getFileUrl();
//                    Log.d(TAG, "Signed URL: " + signedUrl);
//
//                    new Thread(() -> {
//                        try {
//                            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
//                            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(
//                                    okhttp3.MediaType.parse(mimeType),
//                                    file
//                            );
//
//                            okhttp3.Request request = new okhttp3.Request.Builder()
//                                    .url(signedUrl)
//                                    .put(requestBody)
//                                    .build();
//
//                            okhttp3.Response s3Response = client.newCall(request).execute();
//                            if (s3Response.isSuccessful()) {
//                                runOnUiThread(() -> {
//                                    Toast.makeText(CompanyEditProfile.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
//                                    sendProfileData(token, fileUrl);
//                                });
//                            } else {
//                                runOnUiThread(() -> Toast.makeText(CompanyEditProfile.this, "S3 upload failed", Toast.LENGTH_SHORT).show());
//                            }
//                        } catch (Exception e) {
//                            runOnUiThread(() -> Toast.makeText(CompanyEditProfile.this, "Upload error: " + e.getMessage(), Toast.LENGTH_LONG).show());
//                        }
//                    }).start();
//                } else {
//                    Toast.makeText(CompanyEditProfile.this, "Failed to get signed URL", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SignedUrlResponse> call, Throwable t) {
//                Toast.makeText(CompanyEditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void sendProfileData(String token, @Nullable String uploadedFileUrl) {
//        try {
//            org.json.JSONObject jsonObject = new org.json.JSONObject();
//            jsonObject.put("company_name", etCompanyName.getText().toString().trim());
//            jsonObject.put("company_type", spCompanyType.getSelectedItem() != null ? spCompanyType.getSelectedItem().toString() : "");
//            jsonObject.put("company_size", spCompanySize.getSelectedItem() != null ? spCompanySize.getSelectedItem().toString() : "");
//            jsonObject.put("email", etEmail.getText().toString().trim());
//            jsonObject.put("phone_number", etPhone.getText().toString().trim());
//            jsonObject.put("location", etLocation.getText().toString().trim());
//            jsonObject.put("nation_state", etState.getText().toString().trim());
//            jsonObject.put("website", etWebsite.getText().toString().trim());
//            jsonObject.put("linkedin", etLinkedIn.getText().toString().trim());
//            jsonObject.put("certificate_number", etCertificateNumber.getText().toString().trim());
//            jsonObject.put("description", etAboutCompany.getText().toString().trim());
//
//            int selectedKycId = rgKycMethod.getCheckedRadioButtonId();
//            String kycMethod = "None";
//            if (selectedKycId == rbGST.getId()) kycMethod = "GST Certificate";
//            else if (selectedKycId == rbCOI.getId()) kycMethod = "Certificate of Incorporation";
//            jsonObject.put("kyc_method", kycMethod);
//
//            jsonObject.put("certificate_file_url", uploadedFileUrl != null ? uploadedFileUrl : "");
//
//            RequestBody requestBody = RequestBody.create(
//                    MediaType.parse("application/json; charset=utf-8"),
//                    jsonObject.toString()
//            );
//
//            Call<Void> call = apiService.updateCompanyProfileJson("Bearer " + token, requestBody);
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(CompanyEditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(CompanyEditProfile.this, "Failed to update (" + response.code() + ")", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    Toast.makeText(CompanyEditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error preparing data", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void loadCompanyProfile() {
//        String token = "Bearer " + sessionManager.getToken();
//        apiService.getCompanyProfile().enqueue(new Callback<CompanyProfileResponse>() {
//            @Override
//            public void onResponse(Call<CompanyProfileResponse> call, Response<CompanyProfileResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    CompanyProfileResponse company = response.body();
//                    etCompanyName.setText(company.getCompanyName());
//                    etEmail.setText(company.getEmail());
//                    etLocation.setText(company.getLocation());
//                    etState.setText(company.getNationState());
//                    etWebsite.setText(company.getWebsite());
//                    etPhone.setText(company.getPhoneNumber());
//                    etLinkedIn.setText(company.getLinkedin());
//                    etCertificateNumber.setText(company.getCertificateNumber());
//                    etAboutCompany.setText(company.getDescription());
//                } else {
//                    Toast.makeText(CompanyEditProfile.this, "Failed to load company profile", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CompanyProfileResponse> call, Throwable t) {
//                Toast.makeText(CompanyEditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}

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
    private static final String TAG = "CompanyEditProfile";

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
        if (selectedFileUri != null) {
            uploadFileToS3(); // Upload certificate first
        } else {
            sendProfileData(null); // No file selected
        }
    }

    private void uploadFileToS3() {
        String filePath = FileUtils.getPath(this, selectedFileUri);
        if (filePath == null) {
            Toast.makeText(this, "Invalid file path", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);
        String mimeType = getContentResolver().getType(selectedFileUri);

        FileRequestBody fileBody = new FileRequestBody(file.getName(), mimeType);

        apiService.getSignedUrl(fileBody).enqueue(new Callback<SignedUrlResponse>() {
            @Override
            public void onResponse(Call<SignedUrlResponse> call, Response<SignedUrlResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String signedUrl = response.body().getSignedUrl();
                    String fileUrl = response.body().getSignedUrl();

                    new Thread(() -> {
                        try {
                            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
                            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(
                                    okhttp3.MediaType.parse(mimeType),
                                    file
                            );

                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url(signedUrl)
                                    .put(requestBody)
                                    .build();

                            okhttp3.Response s3Response = client.newCall(request).execute();
                            if (s3Response.isSuccessful()) {
                                runOnUiThread(() -> {
                                    Toast.makeText(CompanyEditProfile.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                                    sendProfileData(fileUrl);
                                });
                            } else {
                                runOnUiThread(() -> Toast.makeText(CompanyEditProfile.this, "S3 upload failed", Toast.LENGTH_SHORT).show());
                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> Toast.makeText(CompanyEditProfile.this, "Upload error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                        }
                    }).start();
                } else {
                    Toast.makeText(CompanyEditProfile.this, "Failed to get signed URL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignedUrlResponse> call, Throwable t) {
                Toast.makeText(CompanyEditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendProfileData(@Nullable String uploadedFileUrl) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            jsonObject.put("company_name", etCompanyName.getText().toString().trim());
            jsonObject.put("company_type", spCompanyType.getSelectedItem() != null ? spCompanyType.getSelectedItem().toString() : "");
            jsonObject.put("company_size", spCompanySize.getSelectedItem() != null ? spCompanySize.getSelectedItem().toString() : "");
            jsonObject.put("email", etEmail.getText().toString().trim());
            jsonObject.put("phone_number", etPhone.getText().toString().trim());
            jsonObject.put("location", etLocation.getText().toString().trim());
            jsonObject.put("nation_state", etState.getText().toString().trim());
            jsonObject.put("website", etWebsite.getText().toString().trim());
            jsonObject.put("linkedin", etLinkedIn.getText().toString().trim());
            jsonObject.put("certificate_number", etCertificateNumber.getText().toString().trim());
            jsonObject.put("description", etAboutCompany.getText().toString().trim());

            int selectedKycId = rgKycMethod.getCheckedRadioButtonId();
            String kycMethod = "None";
            if (selectedKycId == rbGST.getId()) kycMethod = "GST Certificate";
            else if (selectedKycId == rbCOI.getId()) kycMethod = "Certificate of Incorporation";
            jsonObject.put("kyc_method", kycMethod);

            jsonObject.put("certificate_file_url", uploadedFileUrl != null ? uploadedFileUrl : "");

            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonObject.toString()
            );

            apiService.updateCompanyProfile(requestBody).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CompanyEditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CompanyEditProfile.this, "Failed to update (" + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CompanyEditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error preparing data", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCompanyProfile() {
        apiService.getCompanyProfile().enqueue(new Callback<CompanyProfileResponse>() {
            @Override
            public void onResponse(Call<CompanyProfileResponse> call, Response<CompanyProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CompanyProfileResponse company = response.body();
                    etCompanyName.setText(company.getCompanyName());
                    etEmail.setText(company.getEmail());
                    etLocation.setText(company.getLocation());
                    etState.setText(company.getNationState());
                    etWebsite.setText(company.getWebsite());
                    etPhone.setText(company.getPhoneNumber());
                    etLinkedIn.setText(company.getLinkedin());
                    etCertificateNumber.setText(company.getCertificateNumber());
                    etAboutCompany.setText(company.getDescription());
                } else {
                    Toast.makeText(CompanyEditProfile.this, "Failed to load company profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyProfileResponse> call, Throwable t) {
                Toast.makeText(CompanyEditProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
