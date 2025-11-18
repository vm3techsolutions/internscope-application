package com.interns.internscopeapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.JsonObject;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyJobActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1001;

    private int jobId;
    private int companyId;
    private ProgressBar progressBar;
    private Button btnChooseFile, btnSubmit;
    private TextView tvSelectedFile;
    private Uri selectedFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);

        progressBar = findViewById(R.id.progressBar);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvSelectedFile = findViewById(R.id.tvSelectedFile);

        // Get jobId and companyId from intent
        jobId = getIntent().getIntExtra("job_id", 0);
        companyId = getIntent().getIntExtra("company_id", 0);
        if (jobId == 0 || companyId == 0) {
            Toast.makeText(this, "Job or Company ID missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnChooseFile.setOnClickListener(v -> openFileChooser());
        btnSubmit.setOnClickListener(v -> submitApplication());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Resume"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            tvSelectedFile.setText(getFileName(selectedFileUri));
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) result = cursor.getString(index);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) result = result.substring(cut + 1);
        }
        return result;
    }

    private void submitApplication() {
        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        try {
            SessionManager sessionManager = new SessionManager(this);
            String token = sessionManager.getActiveToken();
            String fullNameStr = sessionManager.getFullName();
            String emailStr = sessionManager.getEmail();

            if (fullNameStr == null || emailStr == null) {
                Toast.makeText(this, "User details missing.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                return;
            }

            if (selectedFileUri != null) {
                InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                String mimeType = getContentResolver().getType(selectedFileUri);
                if (mimeType == null) mimeType = "application/octet-stream";

                MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                        "resume",
                        getFileName(selectedFileUri),
                        RequestBody.create(MediaType.parse(mimeType), bytes)
                );

                ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
                Call<UploadResponse> uploadCall = apiService.uploadResume("Bearer " + token, filePart);

                uploadCall.enqueue(new Callback<UploadResponse>() {
                    @Override
                    public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String resumeUrl = response.body().getUrl();
                            Log.d("APPLY_JOB", "Resume uploaded URL: " + resumeUrl);

                            // --- Step 2: Submit application with JSON ---
                            JsonObject body = new JsonObject();
                            body.addProperty("fullName", fullNameStr);
                            body.addProperty("email", emailStr);
                            body.addProperty("company_id", companyId);
                            body.addProperty("resume_url", resumeUrl);

                            Call<ApplyJobResponse> applyCall = apiService.applyJobWithJson(
                                    "Bearer " + token,
                                    jobId,
                                    body
                            );

                            applyCall.enqueue(new Callback<ApplyJobResponse>() {
                                @Override
                                public void onResponse(Call<ApplyJobResponse> call, Response<ApplyJobResponse> response) {
                                    progressBar.setVisibility(View.GONE);
                                    btnSubmit.setEnabled(true);

                                    if (response.isSuccessful()) {
                                        Toast.makeText(ApplyJobActivity.this, "Applied successfully!", Toast.LENGTH_SHORT).show();
                                        sendResultBack(true);
                                    } else if (response.code() == 409) { // Already applied
                                        Toast.makeText(ApplyJobActivity.this, "You have already applied for this job", Toast.LENGTH_SHORT).show();
                                        sendResultBack(true);
                                    } else {
                                        Log.e("API_ERROR", "Apply response code: " + response.code());
                                        Toast.makeText(ApplyJobActivity.this, "Failed to apply", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApplyJobResponse> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                    btnSubmit.setEnabled(true);
                                    Toast.makeText(ApplyJobActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            btnSubmit.setEnabled(true);
                            Toast.makeText(ApplyJobActivity.this, "Resume upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        btnSubmit.setEnabled(true);
                        Toast.makeText(ApplyJobActivity.this, "Upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "Please select a resume", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            btnSubmit.setEnabled(true);
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendResultBack(boolean applied) {
        Intent intent = new Intent();
        intent.putExtra("applied", applied);
        setResult(RESULT_OK, intent);
        finish();
    }
}
