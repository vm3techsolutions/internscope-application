//package com.example.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputEditText;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class EditProfiles extends AppCompatActivity {
//
//    private TextInputEditText etFirstName, etLastName, etLocation, etNationState, etPhone, etEmail,
//            etWebsiteLink, etCurrentJobPlace, etDesignation, etQualification, etLanguage, etDescription;
//    private RadioGroup rgGender;
//    private MaterialButton btnSave;
//
//    private SessionManager sessionManager;
//    private ApiService apiService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_editprofiles);
//
//        sessionManager = SessionManager.getInstance(this);
//        apiService = ApiClient.getClient(this).create(ApiService.class);
//
//        etFirstName = findViewById(R.id.etFirstName);
//        etLastName = findViewById(R.id.etLastName);
//        etLocation = findViewById(R.id.etLocation);
//        etNationState = findViewById(R.id.etNationState);
//        etPhone = findViewById(R.id.etPhone);
//        etEmail = findViewById(R.id.etEmail);
//        etWebsiteLink = findViewById(R.id.etLinkedIn);
//        etCurrentJobPlace = findViewById(R.id.etCurrentJobPlace);
//        etDesignation = findViewById(R.id.etDesignation);
//        etQualification = findViewById(R.id.etQualification);
//        etLanguage = findViewById(R.id.etLanguage);
//        etDescription = findViewById(R.id.etDescription);
//
//        rgGender = findViewById(R.id.rgGender);
//        btnSave = findViewById(R.id.btnSave);
//
//        loadUserProfile();
//
//        btnSave.setOnClickListener(v -> saveProfile());
//    }
//
//    private void loadUserProfile() {
//        String token = "Bearer " + sessionManager.getActiveToken();
//        apiService.getUserProfile().enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    User user = response.body();
//                    etFirstName.setText(user.getFirstName());
//                    etLastName.setText(user.getLastName());
//                    etLocation.setText(user.getCurrentLocation());
//                    etNationState.setText(user.getNationState());
//                    etPhone.setText(user.getPhone());
//                    etEmail.setText(user.getEmail());
//                    etWebsiteLink.setText(user.getLinkedin());
//                    etCurrentJobPlace.setText(user.getCurrentJobPlace());
//                    etDesignation.setText(user.getDesignation());
//                    etQualification.setText(user.getQualification());
//                    etLanguage.setText(user.getLanguage());
//                    etDescription.setText(user.getDescription());
//
//                    if ("Male".equalsIgnoreCase(user.getGender())) rgGender.check(R.id.rbMale);
//                    else if ("Female".equalsIgnoreCase(user.getGender())) rgGender.check(R.id.rbFemale);
//                    else if ("Other".equalsIgnoreCase(user.getGender())) rgGender.check(R.id.rbOther);
//
//                } else {
//                    Toast.makeText(EditProfiles.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(EditProfiles.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveProfile() {
//        String token = "Bearer " + sessionManager.getActiveToken();
//
//        int selectedGenderId = rgGender.getCheckedRadioButtonId();
//        RadioButton selectedGenderButton = findViewById(selectedGenderId);
//        String genderValue = selectedGenderButton != null ? selectedGenderButton.getText().toString() : "";
//
//        UserRequest userRequest = new UserRequest(
//                etFirstName.getText().toString(),
//                etLastName.getText().toString(),
//                etLocation.getText().toString(),
//                etNationState.getText().toString(),
//                etPhone.getText().toString(),
//                etEmail.getText().toString(),
//                etWebsiteLink.getText().toString(),
//                etCurrentJobPlace.getText().toString(),
//                etDesignation.getText().toString(),
//                etQualification.getText().toString(),
//                etLanguage.getText().toString(),
//                genderValue,
//                etDescription.getText().toString()
//        );
//
//        apiService.updateProfile(token, userRequest).enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(EditProfiles.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
//
//                    // Redirect to Profile activity
//                    Intent intent = new Intent(EditProfiles.this, Profile.class);
//                    startActivity(intent);
//                    finish(); // optional: close EditProfiles activity
//                } else {
//                    Toast.makeText(EditProfiles.this, "Update failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                Toast.makeText(EditProfiles.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}

package com.example.internscopeapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.InputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
//import okhttp3.Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfiles extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 200;

    private TextInputEditText etFirstName, etLastName, etLocation, etNationState, etPhone, etEmail,
            etWebsiteLink, etCurrentJobPlace, etDesignation, etQualification, etLanguage, etDescription;

    private RadioGroup rgGender;
    private MaterialButton btnSave;
     private Button btnChooseFile;

    Uri selectedPDFUri = null;
    String uploadedResumeKey = "";

    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofiles);

        sessionManager = SessionManager.getInstance(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etLocation = findViewById(R.id.etLocation);
        etNationState = findViewById(R.id.etNationState);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etWebsiteLink = findViewById(R.id.etLinkedIn);
        etCurrentJobPlace = findViewById(R.id.etCurrentJobPlace);
        etDesignation = findViewById(R.id.etDesignation);
        etQualification = findViewById(R.id.etQualification);
        etLanguage = findViewById(R.id.etLanguage);
        etDescription = findViewById(R.id.etDescription);

        rgGender = findViewById(R.id.rgGender);
        btnSave = findViewById(R.id.btnSave);
        btnChooseFile = findViewById(R.id.btnChooseFile);

        loadUserProfile();

        btnChooseFile.setOnClickListener(v -> openFilePicker());
        btnSave.setOnClickListener(v -> saveProfile());
    }

    // ------------------------------
    // STEP 1: Select Resume
    // ------------------------------
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select Resume PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedPDFUri = data.getData();
            Toast.makeText(this, "Resume selected", Toast.LENGTH_SHORT).show();

            uploadResumeToS3();
        }
    }

    // ------------------------------
    // STEP 2: Request Signed URL
    // ------------------------------
    private void uploadResumeToS3() {
        if (selectedPDFUri == null) return;

        String fileName = getFileName(selectedPDFUri);
        ResumeFileRequest request = new ResumeFileRequest(fileName);

        String token = "Bearer " + sessionManager.getActiveToken();

        apiService.getSignedUrl(token, request).enqueue(new Callback<SignedUrlResponse>() {
            @Override
            public void onResponse(Call<SignedUrlResponse> call, Response<SignedUrlResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    uploadedResumeKey = response.body().getS3Key();
                    uploadFileToSignedUrl(response.body().getSignedUrl());
                } else {
                    //Toast.makeText(EditProfiles.this, "Failed to get signed URL", Toast.LENGTH_SHORT).show();
                    try {
                        Toast.makeText(EditProfiles.this,
                                "Error: " + response.errorBody().string(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SignedUrlResponse> call, Throwable t) {
                Toast.makeText(EditProfiles.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------------------------------
    // STEP 3: Upload resume to AWS S3
    // ------------------------------
    private void uploadFileToSignedUrl(String signedUrl) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedPDFUri);
            byte[] pdfBytes = new byte[inputStream.available()];
            inputStream.read(pdfBytes);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(pdfBytes, MediaType.parse("application/pdf"));

            Request request = new Request.Builder()
                    .url(signedUrl)
                    .put(requestBody)
                    .build();

            new Thread(() -> {
                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(this, "Resume uploaded!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Upload failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();

        } catch (IOException e) {
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return "";
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }

    // ------------------------------
    // STEP 4: Save Profile
    // ------------------------------
    private void saveProfile() {

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String genderValue = selectedGenderButton != null ? selectedGenderButton.getText().toString() : "";

        UserRequest userRequest = new UserRequest(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etLocation.getText().toString(),
                etNationState.getText().toString(),
                etPhone.getText().toString(),
                etEmail.getText().toString(),
                etWebsiteLink.getText().toString(),
                etCurrentJobPlace.getText().toString(),
                etDesignation.getText().toString(),
                etQualification.getText().toString(),
                etLanguage.getText().toString(),
                uploadedResumeKey,   // ✅ Added this line
                genderValue,
                etDescription.getText().toString()
        );

        String token = "Bearer " + sessionManager.getActiveToken();

        apiService.updateProfile(token, userRequest).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfiles.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfiles.this, Profile.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProfiles.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditProfiles.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfile() {
        apiService.getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();

                    etFirstName.setText(user.getFirstName());
                    etLastName.setText(user.getLastName());
                    etLocation.setText(user.getCurrentLocation());
                    etNationState.setText(user.getNationState());
                    etPhone.setText(user.getPhone());
                    etEmail.setText(user.getEmail());
                    etWebsiteLink.setText(user.getLinkedin());
                    etCurrentJobPlace.setText(user.getCurrentJobPlace());
                    etDesignation.setText(user.getDesignation());
                    etQualification.setText(user.getQualification());
                    etLanguage.setText(user.getLanguage());
                    etDescription.setText(user.getDescription());

                    if ("Male".equalsIgnoreCase(user.getGender())) rgGender.check(R.id.rbMale);
                    else if ("Female".equalsIgnoreCase(user.getGender())) rgGender.check(R.id.rbFemale);
                    else if ("Other".equalsIgnoreCase(user.getGender())) rgGender.check(R.id.rbOther);

                    // Load resume from backend if needed:
                    uploadedResumeKey = user.getResume();

                } else {
                    Toast.makeText(EditProfiles.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfiles.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
