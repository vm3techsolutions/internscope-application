package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfiles extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etLocation, etNationState, etPhone, etEmail,
            etWebsiteLink, etCurrentJobPlace, etDesignation, etQualification, etLanguage, etDescription;
    private RadioGroup rgGender;
    private MaterialButton btnSave;

    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofiles);

        sessionManager = new SessionManager(this);
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

        loadUserProfile();

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadUserProfile() {
        String token = "Bearer " + sessionManager.getToken();
        apiService.getUserProfile(token).enqueue(new Callback<User>() {
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

                } else {
                    Toast.makeText(EditProfiles.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfiles.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        String token = "Bearer " + sessionManager.getToken();

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
                genderValue,
                etDescription.getText().toString()
        );

        apiService.updateProfile(token, userRequest).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditProfiles.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                    // Redirect to Profile activity
                    Intent intent = new Intent(EditProfiles.this, Profile.class);
                    startActivity(intent);
                    finish(); // optional: close EditProfiles activity
                } else {
                    Toast.makeText(EditProfiles.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditProfiles.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
