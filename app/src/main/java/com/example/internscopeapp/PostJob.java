//package com.example.internscopeapp;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class PostJob extends AppCompatActivity {
//
//    private EditText editJobTitle, editLocation, editQualification, editMinSalary, editMaxSalary,
//            editSkills, editDeadline, editDescription;
//    private Spinner spinnerIndustry, spinnerJobType;
//    private Button btnSubmitJob;
//    private CheckBox checkboxTerms;
//
//    private ApiService apiService;
//    private SessionManager sessionManager;
//
//    private String selectedIndustry = "";
//    private String selectedJobType = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_job);
//
//        sessionManager = SessionManager.getInstance(this);
//        String token = sessionManager.getToken();
//
//        initViews();
//        setupSpinners();
//        setupRetrofit();
//
//        // Date picker
//        editDeadline.setOnClickListener(v -> showDatePickerDialog());
//
//        // Submit button
//        btnSubmitJob.setOnClickListener(v -> submitJobPost(token));
//    }
//
//    private void initViews() {
//        editJobTitle = findViewById(R.id.editJobTitle);
//        editLocation = findViewById(R.id.editLocation);
//        editQualification = findViewById(R.id.editQualification);
//        editMinSalary = findViewById(R.id.editMinSalary);
//        editMaxSalary = findViewById(R.id.editMaxSalary);
//        editSkills = findViewById(R.id.editSkills);
//        editDeadline = findViewById(R.id.editDeadline);
//        editDescription = findViewById(R.id.editDescription);
//        spinnerIndustry = findViewById(R.id.spinnerIndustry);
//        spinnerJobType = findViewById(R.id.spinnerJobType);
//        checkboxTerms = findViewById(R.id.checkboxTerms);
//        btnSubmitJob = findViewById(R.id.btnSubmitJob);
//    }
//
//    private void setupSpinners() {
//        // Industry
//        ArrayAdapter<CharSequence> industryAdapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.company_types,
//                android.R.layout.simple_spinner_item
//        );
//        industryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerIndustry.setAdapter(industryAdapter);
//
//        spinnerIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedIndustry = adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {}
//        });
//
//        // Job Type
//        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_spinner_item,
//                new String[]{"Select Job Type", "Full Time", "Part Time", "Internship"}
//        );
//        jobTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerJobType.setAdapter(jobTypeAdapter);
//
//        spinnerJobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedJobType = adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {}
//        });
//    }
//
//    private void setupRetrofit() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.104:4000/api/") // Local backend URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        apiService = retrofit.create(ApiService.class);
//    }
//
//    private void showDatePickerDialog() {
//        final Calendar calendar = Calendar.getInstance();
//        new DatePickerDialog(this, (view, y, m, d) -> {
//            editDeadline.setText(String.format("%04d-%02d-%02d", y, m + 1, d));
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//    }
//
//    private void submitJobPost(String token) {
//        // Read input
//        String jobTitle = editJobTitle.getText().toString().trim();
//        String location = editLocation.getText().toString().trim();
//        String qualification = editQualification.getText().toString().trim();
//        String minSalary = editMinSalary.getText().toString().trim();
//        String maxSalary = editMaxSalary.getText().toString().trim();
//        String skills = editSkills.getText().toString().trim();
//        String deadline = editDeadline.getText().toString().trim();
//        String description = editDescription.getText().toString().trim();
//        boolean termsAccepted = checkboxTerms.isChecked();
//
//        // Validate
//        if (jobTitle.isEmpty() || location.isEmpty() || qualification.isEmpty()
//                || minSalary.isEmpty() || maxSalary.isEmpty() || skills.isEmpty()
//                || description.isEmpty() || selectedIndustry.equals("Select Company Category")
//                || selectedJobType.equals("Select Job Type")) {
//            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!termsAccepted) {
//            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Convert skills to list
//        List<String> jobTags = Arrays.asList(skills.split(",\\s*"));
//
//        // Build request
//        JobPostRequest jobPost = new JobPostRequest(
//                //sessionManager.getUserId(), // Pass company_id from session
//                0,
//                jobTitle,
//                selectedIndustry,
//                location,
//                qualification,
//                Integer.parseInt(minSalary),
//                Integer.parseInt(maxSalary),
//                "rangePrice",
//                selectedJobType,
//                jobTags,
//                deadline,
//                description,
//                termsAccepted
//        );
//
//        // Call API
//        apiService.createJobPost("Bearer " + token, jobPost).enqueue(new Callback<UpdateResponse>() {
//            @Override
//            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(PostJob.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    Toast.makeText(PostJob.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UpdateResponse> call, Throwable t) {
//                Toast.makeText(PostJob.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}

package com.example.internscopeapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostJob extends AppCompatActivity {

    private EditText editJobTitle, editLocation, editQualification, editMinSalary, editMaxSalary, editSkills, editDeadline, editDescription;
    private Spinner spinnerIndustry, spinnerJobType;
    private Button btnSubmitJob;
    private CheckBox checkboxTerms;

    private ApiService apiService;
    private SessionManager sessionManager;

    private String selectedIndustry, selectedJobType;
    private int companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        // Initialize SessionManager
        sessionManager = SessionManager.getInstance(this);
        int userId = sessionManager.getUserId();
        String token = sessionManager.getActiveToken();

        // Use companyId same as userId
        companyId = userId;

        // Initialize Views
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
        checkboxTerms = findViewById(R.id.checkboxTerms);
        btnSubmitJob = findViewById(R.id.btnSubmitJob);

        // Retrofit Setup - ✅ Correct base URL (with trailing slash)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.104:4000/api/") // correct base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Load Industry Spinner
        ArrayAdapter<CharSequence> industryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.company_types,
                android.R.layout.simple_spinner_item
        );
        industryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIndustry.setAdapter(industryAdapter);
        spinnerIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndustry = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // Load Job Type Spinner
        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Select Job Type", "Full Time", "Part Time", "Internship"}
        );
        jobTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobType.setAdapter(jobTypeAdapter);
        spinnerJobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedJobType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // Date Picker
        editDeadline.setOnClickListener(v -> showDatePickerDialog());

        // Submit Button
        btnSubmitJob.setOnClickListener(v -> submitJobPost(token));
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(PostJob.this, (DatePicker view, int y, int m, int d) -> {
            String date = y + "-" + (m + 1) + "-" + d; // yyyy-MM-dd
            editDeadline.setText(date);
        }, year, month, day);
        dialog.show();
    }

    private void submitJobPost(String token) {
        String jobTitle = editJobTitle.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String qualification = editQualification.getText().toString().trim();
        String minSalary = editMinSalary.getText().toString().trim();
        String maxSalary = editMaxSalary.getText().toString().trim();
        String skills = editSkills.getText().toString().trim();
        String deadline = editDeadline.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        boolean termsAccepted = checkboxTerms.isChecked();

        if (jobTitle.isEmpty() || location.isEmpty() || qualification.isEmpty() ||
                minSalary.isEmpty() || maxSalary.isEmpty() || skills.isEmpty() ||
                description.isEmpty() || selectedIndustry.equals("Select Company Category")) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!termsAccepted) {
            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert skills to List<String>
        List<String> jobTags = Arrays.asList(skills.split(",\\s*"));

        // Create JobPostRequest
        JobPostRequest jobPost = new JobPostRequest(
                companyId,           // ✅ include company_id
                jobTitle,
                selectedIndustry,
                location,
                qualification,
                Integer.parseInt(minSalary),
                Integer.parseInt(maxSalary),
                "rangePrice",
                selectedJobType,
                jobTags,
                deadline,
                description,
                termsAccepted
        );

        // Call API
        apiService.createJobPost("Bearer " + token, jobPost).enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(PostJob.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PostJob.this, "You have reached your job posting limit.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                Toast.makeText(PostJob.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
