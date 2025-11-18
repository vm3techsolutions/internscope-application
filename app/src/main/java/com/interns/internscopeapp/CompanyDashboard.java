//package com.interns.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class CompanyDashboard extends AppCompatActivity {
//
//    private static final String TAG = "CompanyDashboard";
//
//    private TextView tvPurchasedJobs, tvUsedJobs, tvAvailableJobs, tvTotalApplied, tvUserName;
//    private ProgressBar progressBar;
//    private RecyclerView recyclerAppliedCandidates;
//    private Button btnEditProfile;
//    private SwipeRefreshLayout swipeRefreshLayout;
//
//    private AppliedCandidateAdapter adapter;
//    private List<CandidateResponse> candidateList = new ArrayList<>();
//
//    private SessionManager sessionManager;
//    private String authToken; // raw token (without "Bearer ")
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_company_dashboard);
//
//        // Initialize views
//        tvPurchasedJobs = findViewById(R.id.tvPurchasedJobs);
//        tvUsedJobs = findViewById(R.id.tvUsedJobs);
//        tvAvailableJobs = findViewById(R.id.tvAvailableJobs);
//        tvTotalApplied = findViewById(R.id.tvTotalApplied);
//        tvUserName = findViewById(R.id.tvUserName);
//        progressBar = findViewById(R.id.progressBar);
//        recyclerAppliedCandidates = findViewById(R.id.recyclerAppliedCandidates);
//        btnEditProfile = findViewById(R.id.btnEditProfile);
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//
//        // Setup session manager
//        sessionManager = SessionManager.getInstance(this);
//        authToken = sessionManager.getToken(); // e.g. "eyJ..."; not "Bearer ..."
//
//        if (authToken == null || authToken.isEmpty()) {
//            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, Company_login.class); // your login activity
//            startActivity(intent);
//            finish();
//            return;
//        }
//
//        // Setup RecyclerView
//        recyclerAppliedCandidates.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new AppliedCandidateAdapter(this, candidateList);
//        recyclerAppliedCandidates.setAdapter(adapter);
//
//        // Edit Profile button
//        btnEditProfile.setOnClickListener(v -> {
//            Intent intent = new Intent(CompanyDashboard.this, CompanyEditProfile.class);
//            startActivity(intent);
//        });
//
//        // Swipe to refresh
//        swipeRefreshLayout.setOnRefreshListener(this::loadDashboardData);
//
//        // Load data initially
//        loadDashboardData();
//    }
//
//    // ==================== CENTRAL REFRESH METHOD ====================
//    private void loadDashboardData() {
//        progressBar.setVisibility(View.VISIBLE);
//        // fetch profile + stats + candidates in parallel
//        fetchCompanyProfile();
//        fetchDashboardStats();
//        //fetchAppliedCandidates();
//    }
//
//    // ==================== FETCH COMPANY PROFILE ====================
//    private void fetchCompanyProfile() {
//        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
//        Call<CompanyProfileResponse> call = apiService.getCompanyProfile();
//
//        call.enqueue(new Callback<CompanyProfileResponse>() {
//            @Override
//            public void onResponse(Call<CompanyProfileResponse> call, Response<CompanyProfileResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    CompanyProfileResponse profile = response.body();
//                    tvUserName.setText(profile.getCompanyName() != null ? profile.getCompanyName() : "Company User");
//                } else {
//                    tvUserName.setText("Company User");
//                    Log.w(TAG, "fetchCompanyProfile: failed, code=" + response.code());
//                }
//                stopRefreshing();
//            }
//
//            @Override
//            public void onFailure(Call<CompanyProfileResponse> call, Throwable t) {
//                tvUserName.setText("Company User");
//                Log.e(TAG, "fetchCompanyProfile error", t);
//                stopRefreshing();
//            }
//        });
//    }
//
//    // ==================== FETCH DASHBOARD STATS ====================
//    private void fetchDashboardStats() {
//        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
//        Call<DashboardStatsResponse> call = apiService.getDashboardStats("Bearer " + authToken);
//
//        call.enqueue(new Callback<DashboardStatsResponse>() {
//            @Override
//            public void onResponse(Call<DashboardStatsResponse> call, Response<DashboardStatsResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    DashboardStatsResponse stats = response.body();
//
//                    tvPurchasedJobs.setText(String.valueOf(stats.getPurchasedJobs()));
//                    tvUsedJobs.setText(String.valueOf(stats.getUsedJobs()));
//                    tvAvailableJobs.setText(String.valueOf(stats.getAvailableJobs()));
//
//                    // ✅ Now fetch candidates using stats
//                    fetchAppliedCandidates(stats);
//                } else {
//                    Toast.makeText(CompanyDashboard.this,
//                            "Failed to load stats (" + response.code() + ")", Toast.LENGTH_SHORT).show();
//                    stopRefreshing();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DashboardStatsResponse> call, Throwable t) {
//                Toast.makeText(CompanyDashboard.this,
//                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                stopRefreshing();
//            }
//        });
//    }
//
//
//    // ==================== FETCH APPLIED CANDIDATES ====================
//    private void fetchAppliedCandidates(DashboardStatsResponse stats) {
//        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
//        Call<List<CandidateResponse>> call = apiService.getAppliedCandidates("Bearer " + authToken);
//
//        call.enqueue(new Callback<List<CandidateResponse>>() {
//            @Override
//            public void onResponse(Call<List<CandidateResponse>> call, Response<List<CandidateResponse>> response) {
//                progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful() && response.body() != null) {
//                    candidateList.clear();
//                    candidateList.addAll(response.body());
//                    adapter.notifyDataSetChanged();
//
//                    int totalAppliedCount = candidateList.size();
//                    tvPurchasedJobs.setText(String.valueOf(stats.getPurchasedJobs()));
//                    tvUsedJobs.setText(String.valueOf(stats.getUsedJobs()));
//                    tvAvailableJobs.setText(String.valueOf(stats.getAvailableJobs()));
//                    tvTotalApplied.setText(String.valueOf(totalAppliedCount));
//
//                } else {
//                    Toast.makeText(CompanyDashboard.this, "No candidates found", Toast.LENGTH_SHORT).show();
//                    tvTotalApplied.setText("0");
//                }
//                stopRefreshing();
//            }
//
//            @Override
//            public void onFailure(Call<List<CandidateResponse>> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(CompanyDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                tvTotalApplied.setText("0");
//                stopRefreshing();
//            }
//        });
//    }
//
//
//    // ==================== STOP REFRESH INDICATOR ====================
//    private void stopRefreshing() {
//        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//            swipeRefreshLayout.setRefreshing(false);
//        }
//        if (progressBar != null) progressBar.setVisibility(View.GONE);
//    }
//}

package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyDashboard extends BaseActivity {

    private static final String TAG = "CompanyDashboard";

    private TextView tvPurchasedJobs, tvUsedJobs, tvAvailableJobs, tvTotalApplied, tvUserName;
    private ProgressBar progressBar;
    private RecyclerView recyclerAppliedCandidates;
    private Button btnEditProfile;
    private SwipeRefreshLayout swipeRefreshLayout;

    private AppliedCandidateAdapter adapter;
    private List<CandidateResponse> candidateList = new ArrayList<>();

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_dashboard);

        // Initialize views
        tvPurchasedJobs = findViewById(R.id.tvPurchasedJobs);
        tvUsedJobs = findViewById(R.id.tvUsedJobs);
        tvAvailableJobs = findViewById(R.id.tvAvailableJobs);
        tvTotalApplied = findViewById(R.id.tvTotalApplied);
        tvUserName = findViewById(R.id.tvUserName);
        progressBar = findViewById(R.id.progressBar);
        recyclerAppliedCandidates = findViewById(R.id.recyclerAppliedCandidates);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Setup ApiService (token auto-handled by AuthInterceptor)
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // Setup RecyclerView
        recyclerAppliedCandidates.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppliedCandidateAdapter(this, candidateList);
        recyclerAppliedCandidates.setAdapter(adapter);

        setupDrawer();

        // Edit Profile
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, CompanyEditProfile.class);
            startActivity(intent);
        });

        // Swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(this::loadDashboardData);

        // Load data initially
        loadDashboardData();
    }

    // ==================== CENTRAL REFRESH METHOD ====================
    private void loadDashboardData() {
        progressBar.setVisibility(View.VISIBLE);
        fetchCompanyProfile();
        fetchDashboardStats();
    }

    // ==================== FETCH COMPANY PROFILE ====================
    private void fetchCompanyProfile() {
        apiService.getCompanyProfile().enqueue(new Callback<CompanyProfileResponse>() {
            @Override
            public void onResponse(Call<CompanyProfileResponse> call, Response<CompanyProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CompanyProfileResponse profile = response.body();
                    tvUserName.setText(nonNull(profile.getCompanyName(), "Company User"));
                } else {
                    tvUserName.setText("Company User");
                    Log.w(TAG, "fetchCompanyProfile failed, code=" + response.code());
                }
                stopRefreshing();
            }

            @Override
            public void onFailure(Call<CompanyProfileResponse> call, Throwable t) {
                tvUserName.setText("Company User");
                Log.e(TAG, "fetchCompanyProfile error: " + t.getMessage());
                stopRefreshing();
            }
        });
    }

    // ==================== FETCH DASHBOARD STATS ====================
    private void fetchDashboardStats() {
        apiService.getDashboardStats().enqueue(new Callback<DashboardStatsResponse>() {
            @Override
            public void onResponse(Call<DashboardStatsResponse> call, Response<DashboardStatsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardStatsResponse stats = response.body();
                    tvPurchasedJobs.setText(String.valueOf(stats.getPurchasedJobs()));
                    tvUsedJobs.setText(String.valueOf(stats.getUsedJobs()));
                    tvAvailableJobs.setText(String.valueOf(stats.getAvailableJobs()));

                    // Fetch candidates next
                    fetchAppliedCandidates(stats);
                } else {
                    Toast.makeText(CompanyDashboard.this,
                            "Failed to load stats (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    stopRefreshing();
                }
            }

            @Override
            public void onFailure(Call<DashboardStatsResponse> call, Throwable t) {
                Toast.makeText(CompanyDashboard.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                stopRefreshing();
            }
        });
    }

    // ==================== FETCH APPLIED CANDIDATES ====================
    private void fetchAppliedCandidates(DashboardStatsResponse stats) {
        apiService.getAppliedCandidates().enqueue(new Callback<List<CandidateResponse>>() {
            @Override
            public void onResponse(Call<List<CandidateResponse>> call, Response<List<CandidateResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    candidateList.clear();
                    candidateList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    tvTotalApplied.setText(String.valueOf(candidateList.size()));
                } else {
                    Toast.makeText(CompanyDashboard.this, "No candidates found", Toast.LENGTH_SHORT).show();
                    tvTotalApplied.setText("0");
                }
                stopRefreshing();
            }

            @Override
            public void onFailure(Call<List<CandidateResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CompanyDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                tvTotalApplied.setText("0");
                stopRefreshing();
            }
        });
    }

    // ==================== STOP REFRESH INDICATOR ====================
    private void stopRefreshing() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    // ==================== UTILS ====================
    private String nonNull(String value, String fallback) {
        return (value != null && !value.isEmpty()) ? value : fallback;
    }
}
