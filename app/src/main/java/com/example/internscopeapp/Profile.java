package com.example.internscopeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    private static final int REQUEST_CODE_GALLERY = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private ImageView imageView;
    private ImageButton setImg;
    private UserDatabseHelper dbHelper;
    private SessionManager sessionManager;
    private FirebaseAuth auth;
    private String currentUsername; // Stores the logged-in username

    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edit = findViewById(R.id.editprofile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(Profile.this, editprofiles.class);
                startActivity(editIntent);
            }
        });

        // Initialize database helper, session manager, and Firebase authentication
        dbHelper = new UserDatabseHelper(this);
        sessionManager = new SessionManager(this);
        auth = FirebaseAuth.getInstance();

        // Initialize views
        imageView = findViewById(R.id.profile_img);
        setImg = findViewById(R.id.setImg);

        // Get the logged-in username from session or Google
        getCurrentUser();

        // Load and display stored profile image
        loadProfileImage();

        // Set up button click listener for setting image
        setImg.setOnClickListener(view -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                // For Android 13+
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                        == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
                }
            } else {
                // For Android 12 and below
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    // Determine the currently logged-in user
    private void getCurrentUser() {
        FirebaseUser googleUser = auth.getCurrentUser();
        String emailUser = sessionManager.getUsername(); // Check session for email user

        if (googleUser != null) {
            currentUsername = googleUser.getEmail(); // Use email as the identifier for Google users
        } else if (emailUser != null && !emailUser.isEmpty()) {
            currentUsername = emailUser; // Use username for email users
        }

        if (currentUsername == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    // Open the gallery using an Intent
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    // Handle the result of the gallery intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GALLERY && data != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap); // Display image in ImageView

                if (currentUsername == null || currentUsername.isEmpty()) {
                    Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save image in database
                boolean success = dbHelper.saveProfileImage(currentUsername, bitmap);
                if (success) {
                    Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to save image!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Load and Display Profile Image
    private void loadProfileImage() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = dbHelper.getProfileImage(currentUsername);
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "No profile image found!", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle runtime permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "Permission is required to access the gallery.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied! Please enable it in app settings.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
