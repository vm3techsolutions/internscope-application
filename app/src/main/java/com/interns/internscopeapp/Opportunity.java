package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Opportunity extends BaseActivity {

    private RecyclerView recyclerJobs;
    private CompanyJobAdapter jobAdapter;
    private List<CompanyJobModel> jobList = new ArrayList<>();

    private MaterialButton btnCreateJob, btnSearch;
    private EditText etSearchJob;

    private SessionManager sessionManager;
    private ApiService apiService; // Retrofit interface

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity);

        recyclerJobs = findViewById(R.id.recyclerJobs);
        btnCreateJob = findViewById(R.id.btnCreateJob);
        btnSearch = findViewById(R.id.btnSearch);
        etSearchJob = findViewById(R.id.etSearchJob);

        recyclerJobs.setLayoutManager(new LinearLayoutManager(this));

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

         setupDrawer();

        // Set up adapter
        jobAdapter = new CompanyJobAdapter(this, jobList, new CompanyJobAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(CompanyJobModel job) {
                Intent intent = new Intent(Opportunity.this, JobDetailActivity.class);
                intent.putExtra("job_id", job.getId());
                startActivity(intent);
            }

            @Override
            public void onEditClick(CompanyJobModel job) {
                Intent intent = new Intent(Opportunity.this, EditJobActivity.class);
                intent.putExtra("job_id", job.getId());
                startActivity(intent);
            }
        });

        recyclerJobs.setAdapter(jobAdapter);

        // Fetch jobs from API
        fetchCompanyJobs();

        // Add new job button
        btnCreateJob.setOnClickListener(v -> {
            Intent intent = new Intent(Opportunity.this, PostJob.class);
            startActivity(intent);
        });

        // Search jobs by title
        btnSearch.setOnClickListener(v -> {
            String query = etSearchJob.getText().toString().trim();
            if (!query.isEmpty()) {
                searchJobs(query);
            } else {
                fetchCompanyJobs(); // Reload all jobs
            }
        });
    }

    private void fetchCompanyJobs() {
        int companyId = sessionManager.getUserId(); // assuming user_id is stored here
        String token = sessionManager.getActiveToken();

        Log.d("API_JOB_DEBUG", "Fetching jobs for company ID: " + companyId);

        Call<List<CompanyJobModel>> call = apiService.getJobsByCompany("Bearer " + token, companyId);
        call.enqueue(new Callback<List<CompanyJobModel>>() {
            @Override
            public void onResponse(Call<List<CompanyJobModel>> call, Response<List<CompanyJobModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    jobList.clear();
                    jobList.addAll(response.body());
                    jobAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Opportunity.this, "No jobs found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompanyJobModel>> call, Throwable t) {
                Toast.makeText(Opportunity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_JOB_ERROR", "Fetch failed", t);
            }
        });
    }

    private void searchJobs(String query) {
        List<CompanyJobModel> filteredList = new ArrayList<>();
        for (CompanyJobModel job : jobList) {
            if (job.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(job);
            }
        }
        jobAdapter = new CompanyJobAdapter(this, filteredList, jobAdapterListener());
        recyclerJobs.setAdapter(jobAdapter);
    }

    private CompanyJobAdapter.OnItemClickListener jobAdapterListener() {
        return new CompanyJobAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(CompanyJobModel job) {
                Intent intent = new Intent(Opportunity.this, JobDetailActivity.class);
                intent.putExtra("job_id", job.getId());
                startActivity(intent);
            }

            @Override
            public void onEditClick(CompanyJobModel job) {
                Intent intent = new Intent(Opportunity.this, EditJobActivity.class);
                intent.putExtra("job_id", job.getId());
                startActivity(intent);
            }
        };
    }
}
