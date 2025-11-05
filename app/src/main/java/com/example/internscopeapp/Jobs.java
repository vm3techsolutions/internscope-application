package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Jobs extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JobsAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        recyclerView = findViewById(R.id.rvJobs);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchAllJobs();
    }

    private void fetchAllJobs() {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<List<JobResponse>> call = apiService.getAllJobs();

        call.enqueue(new Callback<List<JobResponse>>() {
            @Override
            public void onResponse(Call<List<JobResponse>> call, Response<List<JobResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<JobResponse> jobList = response.body();

                    // Updated adapter call
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
}
