package com.interns.internscopeapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditJobActivity extends AppCompatActivity {

    private EditText editJobTitle, editLocation, editQualification, editMinSalary, editMaxSalary, editSkills, editDeadline, editDescription;
    private Spinner spinnerIndustry, spinnerJobType;
    private Button btnUpdateJob;

    private int jobId;
    private SessionManager sessionManager;
    private ApiService apiService;

    private String selectedIndustry, selectedJobType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);

        editJobTitle = findViewById(R.id.editJobTitle);
        editLocation = findViewById(R.id.editLocation);
        editQualification = findViewById(R.id.editQualification);
        editMinSalary = findViewById(R.id.editMinSalary);
        editMaxSalary = findViewById(R.id.editMaxSalary);
        editSkills = findViewById(R.id.editSkills);
        editDeadline = findViewById(R.id.editDeadline);
        editDescription = findViewById(R.id.editDescription);
        spinnerIndustry = findViewById(R.id.spinnerIndustry);
        spinnerJobType = findViewById(R.id.spinnerJobType);
        btnUpdateJob = findViewById(R.id.btnUpdateJob);

        sessionManager = SessionManager.getInstance(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        jobId = getIntent().getIntExtra("job_id", -1);
        if (jobId == -1) {
            Toast.makeText(this, "Invalid Job ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupSpinners();
        setupDatePicker();
        fetchJobDetails(jobId);

        btnUpdateJob.setOnClickListener(v -> updateJobDetails());
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> industryAdapter = ArrayAdapter.createFromResource(this,
                R.array.company_types, android.R.layout.simple_spinner_item);
        industryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIndustry.setAdapter(industryAdapter);
        spinnerIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedIndustry = parent.getItemAtPosition(pos).toString();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"Full Time", "Part Time", "Internship"}
        );
        jobTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobType.setAdapter(jobTypeAdapter);
        spinnerJobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedJobType = parent.getItemAtPosition(pos).toString();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupDatePicker() {
        editDeadline.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(EditJobActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String date = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                        editDeadline.setText(date);
                    }, year, month, day);
            dialog.show();
        });
    }

    private void fetchJobDetails(int jobId) {
        String token = sessionManager.getActiveToken();
        Log.d("API_EDIT_JOB", "Fetching job details for ID: " + jobId);

        Call<CompanyJobModel> call = apiService.getJobById("Bearer " + token, jobId);
        call.enqueue(new Callback<CompanyJobModel>() {
            @Override
            public void onResponse(Call<CompanyJobModel> call, Response<CompanyJobModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateFields(response.body());
                } else {
                    Toast.makeText(EditJobActivity.this, "Failed to fetch job details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyJobModel> call, Throwable t) {
                Log.e("API_EDIT_JOB", "Error fetching job", t);
                Toast.makeText(EditJobActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields(CompanyJobModel job) {
        editJobTitle.setText(job.getTitle());
        editLocation.setText(job.getLocation());
        editQualification.setText(job.getQualification());
        editMinSalary.setText(String.valueOf(job.getSalaryMin()));
        editMaxSalary.setText(String.valueOf(job.getSalaryMax()));
        if (job.getJobTag() != null && !job.getJobTag().isEmpty()) {
            editSkills.setText(String.join(", ", job.getJobTag()));
        }
        editDeadline.setText(job.getDeadline());
        editDescription.setText(job.getDescription());

        setSpinnerSelection(spinnerIndustry, job.getIndustry());
        setSpinnerSelection(spinnerJobType, job.getJobType());
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value == null) return;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

//    private void updateJobDetails() {
//        String token = sessionManager.getToken();
//
//        // Convert comma-separated skills to JSON array
//        String[] skillsArray = editSkills.getText().toString().split(",");
//        List<String> skillList = new ArrayList<>();
//        for (String s : skillsArray) {
//            if (!s.trim().isEmpty()) {
//                skillList.add(s.trim());
//            }
//        }
//
//        // 🟩 Prepare job data (matching backend field names)
//        Map<String, Object> jobData = new HashMap<>();
//        jobData.put("job_title", getTextOrEmpty(editJobTitle));
//        jobData.put("industry", selectedIndustry);
//        jobData.put("location", getTextOrEmpty(editLocation));
//        jobData.put("qualification", getTextOrEmpty(editQualification));
//        jobData.put("salary_min", safeInt(editMinSalary));
//        jobData.put("salary_max", safeInt(editMaxSalary));
//        jobData.put("job_type", selectedJobType);
//        jobData.put("job_tag", skillList);
//        jobData.put("deadline", getTextOrEmpty(editDeadline));
//        jobData.put("job_description", getTextOrEmpty(editDescription)); // ✅ Correct key
//
//        Log.d("API_EDIT_JOB", "Sending update data: " + jobData);
//
//        Call<Map<String, String>> call = apiService.updateJob("Bearer " + token, jobId, jobData);
//        call.enqueue(new Callback<Map<String, String>>() {
//            @Override
//            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(EditJobActivity.this, "Job updated successfully!", Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    Toast.makeText(EditJobActivity.this, "Failed to update job", Toast.LENGTH_SHORT).show();
//                    Log.e("API_EDIT_JOB", "Response code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Map<String, String>> call, Throwable t) {
//                Toast.makeText(EditJobActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("API_EDIT_JOB", "Update failed", t);
//            }
//        });
//    }
private void updateJobDetails() {
    String token = sessionManager.getActiveToken();

    // Convert skills to List<String>
    String[] skillsArray = editSkills.getText().toString().split(",");
    List<String> skillList = new ArrayList<>();
    for (String s : skillsArray) {
        if (!s.trim().isEmpty()) skillList.add(s.trim());
    }

    // Create updated CompanyJobModel
    CompanyJobModel updatedJob = new CompanyJobModel();
    updatedJob.setTitle(getTextOrEmpty(editJobTitle));
    updatedJob.setIndustry(selectedIndustry);
    updatedJob.setLocation(getTextOrEmpty(editLocation));
    updatedJob.setQualification(getTextOrEmpty(editQualification));
    updatedJob.setSalaryMin(safeInt(editMinSalary));
    updatedJob.setSalaryMax(safeInt(editMaxSalary));
    updatedJob.setJobType(selectedJobType);
    updatedJob.setJobTag(skillList);
    updatedJob.setDeadline(getTextOrEmpty(editDeadline));
    updatedJob.setDescription(getTextOrEmpty(editDescription));

    Log.d("API_EDIT_JOB", "Sending update: " + updatedJob);

    Call<UpdateResponse> call = apiService.updateJobPost(
            "Bearer " + token,
            jobId,
            updatedJob
    );

    call.enqueue(new Callback<UpdateResponse>() {
        @Override
        public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                Toast.makeText(EditJobActivity.this, "Job updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditJobActivity.this, "Failed to update job", Toast.LENGTH_SHORT).show();
                Log.e("API_EDIT_JOB", "Response code: " + response.code());
            }
        }

        @Override
        public void onFailure(Call<UpdateResponse> call, Throwable t) {
            Toast.makeText(EditJobActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("API_EDIT_JOB", "Update failed", t);
        }
    });
}


    private String getTextOrEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        return text.isEmpty() ? "" : text;
    }

    private int safeInt(EditText editText) {
        try {
            return Integer.parseInt(editText.getText().toString().trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
