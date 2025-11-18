package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvJobTitle, tvCompanyId, tvCompanyName, tvLocation, tvSalary, tvJobType, tvJobDescription, tvQualification, tvDeadline;
    private ChipGroup chipGroupTags;
    private MaterialButton btnApplyNow;

    private JobResponse job; // hold the job object
    private static final int APPLY_JOB_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        // --- Initialize views ---
        progressBar = findViewById(R.id.progressBar);
        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvCompanyId = findViewById(R.id.tvCompanyId);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvLocation = findViewById(R.id.tvLocation);
        tvSalary = findViewById(R.id.tvSalary);
        tvJobType = findViewById(R.id.tvJobType);
        tvJobDescription = findViewById(R.id.tvDescription);
        tvQualification = findViewById(R.id.tvQualification);
        tvDeadline = findViewById(R.id.tvDeadline);
        chipGroupTags = findViewById(R.id.chipGroupTags);
        btnApplyNow = findViewById(R.id.btnApplyNow);

        // --- Set button listener once ---
//        btnApplyNow.setOnClickListener(v -> {
//            if (job != null && btnApplyNow.isEnabled()) {
//                Intent intent = new Intent(JobDetailActivity.this, ApplyJobActivity.class);
//                intent.putExtra("job_id", job.getJobId());
//                intent.putExtra("company_id", job.getCompanyId());
//                startActivityForResult(intent, APPLY_JOB_REQUEST);
//            }
//        });

        btnApplyNow.setOnClickListener(v -> {
            if (job != null && btnApplyNow.isEnabled()) {
                applyJobDirectly(job.getJobId(), job.getCompanyId());
            }
        });


        // --- Get job ID from intent ---
        int jobId = getIntent().getIntExtra("job_id", 0);
        if (jobId == 0) {
            Toast.makeText(this, "Job ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Fetch job details ---
        fetchJobDetails(jobId);
    }

    private void fetchJobDetails(int jobId) {
        progressBar.setVisibility(android.view.View.VISIBLE);
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<JobResponse> call = apiService.getJobDetails(jobId);

        call.enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                progressBar.setVisibility(android.view.View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    job = response.body(); // save the job object
                    Log.d("JOB_DETAIL", "Job: " + job.getJobTitle() + ", Company ID: " + job.getCompanyId());

                    // --- Set all dynamic fields ---
                    tvJobTitle.setText(job.getJobTitle());
                    tvCompanyId.setText("Company ID: " + job.getCompanyId());
                    tvCompanyName.setText(job.getCompanyName());
                    tvLocation.setText("📍 " + job.getLocation());
                    tvSalary.setText("💰 ₹ " + job.getSalaryMin() + " - ₹ " + job.getSalaryMax());
                    tvJobType.setText("🕒 " + job.getJobType());
                    tvQualification.setText("🎓 Qualification: " + job.getQualification());

                    // --- Job Description with bullets ---
                    if (job.getJobDescription() != null) {
                        Spanned spannedDescription;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            spannedDescription = Html.fromHtml(job.getJobDescription(), Html.FROM_HTML_MODE_COMPACT, null,
                                    (opening, tag, output, xmlReader) -> {
                                        if (tag.equalsIgnoreCase("li") && opening)
                                            output.append("\n  • ");
                                    });
                        } else {
                            spannedDescription = Html.fromHtml(job.getJobDescription(),
                                    null,
                                    (opening, tag, output, xmlReader) -> {
                                        if (tag.equalsIgnoreCase("li") && opening)
                                            output.append("\n  • ");
                                    });
                        }
                        tvJobDescription.setText(spannedDescription);
                    }

                    // --- Display Tags dynamically ---
                    chipGroupTags.removeAllViews();
                    List<String> tags = job.getJobTag();
                    if (tags != null && !tags.isEmpty()) {
                        for (String tag : tags) {
                            Chip chip = new Chip(JobDetailActivity.this);
                            chip.setText(tag);
                            chip.setClickable(false);
                            chip.setCheckable(false);
                            chip.setChipBackgroundColorResource(R.color.yellow_shade);
                            chip.setTextColor(getResources().getColor(R.color.white));
                            chipGroupTags.addView(chip);
                        }
                    }

                    // --- After job details loaded, check if applied ---
                    checkIfApplied(job.getJobId());

                } else {
                    Toast.makeText(JobDetailActivity.this, "Job not found", Toast.LENGTH_SHORT).show();
                    Log.e("JOB_DETAIL", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(JobDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

//    private void checkIfApplied(int jobId) {
//        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
//        Call<List<AppliedJobResponse>> call = apiService.getAppliedJobs();
//
//        call.enqueue(new Callback<List<AppliedJobResponse>>() {
//            @Override
//            public void onResponse(Call<List<AppliedJobResponse>> call, Response<List<AppliedJobResponse>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    boolean isApplied = false;
//                    for (AppliedJobResponse appliedJob : response.body()) {
//                        if (appliedJob.getJobId() == jobId) {
//                            isApplied = true;
//                            break;
//                        }
//                    }
//
//                    if (isApplied) {
//                        btnApplyNow.setText("Applied");
//                        btnApplyNow.setEnabled(false);
//
//                    } else {
//                        btnApplyNow.setText("Apply Now");
//                        btnApplyNow.setEnabled(true);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<AppliedJobResponse>> call, Throwable t) {
//                Log.e("APPLIED_CHECK", "Error: " + t.getMessage());
//            }
//        });
//    }

    private void checkIfApplied(int jobId) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        String token = SessionManager.getInstance(this).getActiveToken(); // however you store it

        Call<List<AppliedJobResponse>> call = apiService.getAppliedJobs("Bearer " + token);

        call.enqueue(new Callback<List<AppliedJobResponse>>() {
            @Override
            public void onResponse(Call<List<AppliedJobResponse>> call, Response<List<AppliedJobResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isApplied = false;
                    for (AppliedJobResponse appliedJob : response.body()) {
                        if (appliedJob.getJobId() == jobId) {
                            isApplied = true;
                            break;
                        }
                    }

                    if (isApplied) {
                        btnApplyNow.setText("Applied");
                        btnApplyNow.setEnabled(false);
                    } else {
                        btnApplyNow.setText("Apply Now");
                        btnApplyNow.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AppliedJobResponse>> call, Throwable t) {
                Log.e("APPLIED_CHECK", "Error: " + t.getMessage());
            }
        });

    }

    private void fetchResumeFromProfile(ResumeCallback callback) {

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        String token = SessionManager.getInstance(this).getActiveToken();

        Call<User> call = apiService.getUserProfile("Bearer " + token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResumeFetched(response.body().getResume());
                } else {
                    callback.onResumeFetched(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onResumeFetched(null);
            }
        });
    }

    interface ResumeCallback {
        void onResumeFetched(String resumeUrl);
    }


//    // ✅ Handle result from ApplyJobActivity
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == APPLY_JOB_REQUEST && resultCode == RESULT_OK && data != null) {
//            boolean applied = data.getBooleanExtra("applied", false);
//            if (applied) {
//                btnApplyNow.setText("Applied");
//                btnApplyNow.setEnabled(false);
//            }
//        }
//    }

//    private void applyJobDirectly(int jobId, int companyId) {
//        progressBar.setVisibility(android.view.View.VISIBLE);
//
//        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
//        String token = SessionManager.getInstance(this).getActiveToken();
//        // Your saved resume URL (from user profile)
//        String resumeUrl = SessionManager.getInstance(this).getResumeUrl();
//
//        if (resumeUrl == null || resumeUrl.isEmpty()) {
//            Toast.makeText(this, "Please upload your resume first", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Call<GenericResponse> call = apiService.applyJob(
//                "Bearer " + token,
//                jobId,
//                new ApplyJobRequest(resumeUrl, companyId)
//        );
//
//        call.enqueue(new Callback<GenericResponse>() {
//            @Override
//            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
//                progressBar.setVisibility(android.view.View.GONE);
//
//                if (response.isSuccessful()) {
//                    Toast.makeText(JobDetailActivity.this, "Applied Successfully!", Toast.LENGTH_SHORT).show();
//                    btnApplyNow.setText("Applied");
//                    btnApplyNow.setEnabled(false);
//                } else {
//                    Toast.makeText(JobDetailActivity.this, "Already Applied", Toast.LENGTH_SHORT).show();
//                    btnApplyNow.setText("Applied");
//                    btnApplyNow.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GenericResponse> call, Throwable t) {
//                progressBar.setVisibility(android.view.View.GONE);
//                Toast.makeText(JobDetailActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void applyJobDirectly(int jobId, int companyId) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        String token = SessionManager.getInstance(this).getActiveToken();

        if (token == null || token.trim().isEmpty()) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Login again. Token missing.", Toast.LENGTH_LONG).show();
            return;
        }

        // STEP 1: Get profile → fetch resume
        apiService.getUserProfile("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(JobDetailActivity.this, "Failed to load profile", Toast.LENGTH_LONG).show();
                    return;
                }

                User user = response.body();
                String resumeKey = user.getResume();

                if (resumeKey == null || resumeKey.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(JobDetailActivity.this, "Please upload your resume in profile", Toast.LENGTH_LONG).show();
                    return;
                }

                // STEP 2: Build JSON for backend { resume_url, company_id }
                com.google.gson.JsonObject json = new com.google.gson.JsonObject();
                json.addProperty("resume_url", resumeKey);
                json.addProperty("company_id", companyId);

                Log.d("APPLY_DEBUG", "Sending Apply Body: " + json);

                // STEP 3: Send apply request
                apiService.applyJobWithJson("Bearer " + token, jobId, json)
                        .enqueue(new Callback<ApplyJobResponse>() {
                            @Override
                            public void onResponse(Call<ApplyJobResponse> call2, Response<ApplyJobResponse> res) {
                                progressBar.setVisibility(View.GONE);

                                if (res.isSuccessful()) {
                                    btnApplyNow.setText("Applied");
                                    btnApplyNow.setEnabled(false);
                                    Toast.makeText(JobDetailActivity.this, "Applied Successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    int code = res.code();
                                    String err = "";
                                    try {
                                        if (res.errorBody() != null) err = res.errorBody().string();
                                    } catch (Exception ignored) {}

                                    Log.e("APPLY_DEBUG", "Error code:" + code + " body:" + err);

                                    if (code == 409) {
                                        btnApplyNow.setText("Applied");
                                        btnApplyNow.setEnabled(false);
                                        Toast.makeText(JobDetailActivity.this, "Already Applied", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(JobDetailActivity.this,
                                                "Apply failed: " + code + " " + err,
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApplyJobResponse> call2, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Log.e("APPLY_DEBUG", "Network failure: " + t.getMessage());
                                Toast.makeText(JobDetailActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(JobDetailActivity.this, "Profile fetch failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}


