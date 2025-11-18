//package com.interns.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.button.MaterialButton;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class Jobs extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ProgressBar progressBar;
//    private JobsAdapter jobAdapter;
//    private MaterialButton btnSearch;
//
//    private List<CompanyJobModel> jobList = new ArrayList<>();
//    private EditText etSearchJob;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_jobs);
//
//        recyclerView = findViewById(R.id.rvJobs);
//        progressBar = findViewById(R.id.progressBar);
//        btnSearch = findViewById(R.id.btnSearch);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        etSearchJob = findViewById(R.id.etSearchJob);
//
//
//        fetchAllJobs();
//
//
//
//    }
//
//
//    private void fetchAllJobs() {
//        progressBar.setVisibility(View.VISIBLE);
//
//        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
//        Call<List<JobResponse>> call = apiService.getAllJobs();
//
//        call.enqueue(new Callback<List<JobResponse>>() {
//            @Override
//            public void onResponse(Call<List<JobResponse>> call, Response<List<JobResponse>> response) {
//                progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful() && response.body() != null) {
//                    List<JobResponse> jobList = response.body();
//
//                    // Updated adapter call
//                    jobAdapter = new JobsAdapter(Jobs.this, jobList);
//                    recyclerView.setAdapter(jobAdapter);
//
//                } else {
//                    Toast.makeText(Jobs.this, "No jobs found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<JobResponse>> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(Jobs.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("API_ERROR", t.getMessage());
//            }
//        });
//    }
//
//
//
//}

package com.interns.internscopeapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Jobs extends BaseActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText etSearchJob;
    private MaterialButton btnSearch;

    private JobsAdapter jobAdapter;

    // Global job list (from API)
    private List<JobResponse> jobList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        // Initialize views
        recyclerView = findViewById(R.id.rvJobs);
        progressBar = findViewById(R.id.progressBar);
        etSearchJob = findViewById(R.id.etSearchJob);
        btnSearch = findViewById(R.id.btnSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupDrawer();
        // Load jobs from API
        fetchAllJobs();

        // Search button functionality
        btnSearch.setOnClickListener(v -> searchJobs());

        // Live search while typing
        etSearchJob.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchJobs(); // live filter
            }

            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // Fetch all jobs from API
    private void fetchAllJobs() {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<List<JobResponse>> call = apiService.getAllJobs();

        call.enqueue(new Callback<List<JobResponse>>() {
            @Override
            public void onResponse(Call<List<JobResponse>> call, Response<List<JobResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    jobList = response.body(); // store globally

                    // Set adapter
                    jobAdapter = new JobsAdapter(Jobs.this, jobList);
                    recyclerView.setAdapter(jobAdapter);

                } else {
                    Toast.makeText(Jobs.this, "No jobs found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<JobResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Jobs.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    // Search method
    private void searchJobs() {
        String query = etSearchJob.getText().toString().trim().toLowerCase();

        // If empty → show all jobs
        if (query.isEmpty()) {
            jobAdapter = new JobsAdapter(Jobs.this, jobList);
            recyclerView.setAdapter(jobAdapter);
            return;
        }

        List<JobResponse> filteredList = new ArrayList<>();

        for (JobResponse job : jobList) {
            if (job.getJobTitle() != null && job.getJobTitle().toLowerCase().contains(query)) {
                filteredList.add(job);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No matching jobs found", Toast.LENGTH_SHORT).show();
        }

        jobAdapter = new JobsAdapter(Jobs.this, filteredList);
        recyclerView.setAdapter(jobAdapter);
    }
}
