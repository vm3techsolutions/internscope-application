package com.example.internscopeapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
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
        btnApplyNow.setOnClickListener(v -> {
            if (job != null && btnApplyNow.isEnabled()) {
                Intent intent = new Intent(JobDetailActivity.this, ApplyJobActivity.class);
                intent.putExtra("job_id", job.getJobId());
                intent.putExtra("company_id", job.getCompanyId());
                startActivityForResult(intent, APPLY_JOB_REQUEST);
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
                                        if (tag.equalsIgnoreCase("li") && opening) output.append("\n  • ");
                                    });
                        } else {
                            spannedDescription = Html.fromHtml(job.getJobDescription(),
                                    null,
                                    (opening, tag, output, xmlReader) -> {
                                        if (tag.equalsIgnoreCase("li") && opening) output.append("\n  • ");
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
        String token = SessionManager.getInstance(this).getToken(); // however you store it

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


    // ✅ Handle result from ApplyJobActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APPLY_JOB_REQUEST && resultCode == RESULT_OK && data != null) {
            boolean applied = data.getBooleanExtra("applied", false);
            if (applied) {
                btnApplyNow.setText("Applied");
                btnApplyNow.setEnabled(false);
            }
        }
    }
}
