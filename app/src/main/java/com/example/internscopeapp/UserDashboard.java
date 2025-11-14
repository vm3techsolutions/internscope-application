package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDashboard extends BaseActivity {

    private ImageView imgUser;
    private TextView tvHello, tvUserName, tvTotalApplied, tvShortlistedJobs;
    private Button btnEditProfile;
    private RecyclerView recyclerAppliedCandidates;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private AppliedJobsAdapter appliedJobsAdapter;
    private List<AppliedJob> appliedJobList = new ArrayList<>();

    private ApiService apiService;
    private SessionManager sessionManager;
    private int userId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // 🔹 Initialize Views
        imgUser = findViewById(R.id.imgUser);
        tvHello = findViewById(R.id.tvHello);
        tvUserName = findViewById(R.id.tvUserName);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        tvTotalApplied = findViewById(R.id.tvTotalApplied);
        tvShortlistedJobs = findViewById(R.id.tvShortlistedJobs);
        recyclerAppliedCandidates = findViewById(R.id.recyclerAppliedCandidates);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerAppliedCandidates.setLayoutManager(new LinearLayoutManager(this));

        setupDrawer();

        // 🔹 Initialize helpers
        sessionManager = SessionManager.getInstance(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        userId = sessionManager.getUserId();
        token = sessionManager.getActiveToken();

        // 🔹 Set user name
        String userName = sessionManager.getFullName();
        if (userName != null && !userName.isEmpty()) {
            tvUserName.setText(userName);
            tvHello.setText("Hello,");
        }

        // 🔹 Edit Profile click
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserDashboard.this, EditProfiles.class);
            startActivity(intent);
        });

        // 🔹 Swipe refresh listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchAppliedJobs();
        });

        // 🔹 Initial data load
        fetchAppliedJobs();
    }

    // ===============================================
    // 🔹 Fetch Applied Jobs from Backend
    // ===============================================
    private void fetchAppliedJobs() {
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Call<JsonObject> call = apiService.getAppliedJobsByUser(userId, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    JsonArray jobsArray = response.body().getAsJsonArray("jobs");
                    if (jobsArray != null && jobsArray.size() > 0) {
                        Type listType = new TypeToken<List<AppliedJob>>() {}.getType();
                        appliedJobList = new Gson().fromJson(jobsArray, listType);

                        // 🔹 Update RecyclerView
                        appliedJobsAdapter = new AppliedJobsAdapter(UserDashboard.this, appliedJobList);
                        recyclerAppliedCandidates.setAdapter(appliedJobsAdapter);

                        // 🔹 Update counters
                        updateCounters(appliedJobList);
                    } else {
                        appliedJobList.clear();
                        recyclerAppliedCandidates.setAdapter(new AppliedJobsAdapter(UserDashboard.this, appliedJobList));
                        tvTotalApplied.setText("0");
                        tvShortlistedJobs.setText("0");
                        Toast.makeText(UserDashboard.this, "No applied jobs found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserDashboard.this, "Failed to fetch applied jobs (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(UserDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ===============================================
    // 🔹 Update Dashboard Counters
    // ===============================================
    private void updateCounters(List<AppliedJob> jobList) {
        int total = jobList.size();
        int shortlisted = 0;

        for (AppliedJob job : jobList) {
            if (job.getStatus() != null && job.getStatus().equalsIgnoreCase("shortlist")) {
                shortlisted++;
            }
        }

        tvTotalApplied.setText(String.valueOf(total));
        tvShortlistedJobs.setText(String.valueOf(shortlisted));
    }
}
