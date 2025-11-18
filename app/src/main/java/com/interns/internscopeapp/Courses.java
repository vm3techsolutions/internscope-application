package com.interns.internscopeapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.*;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Courses extends BaseActivity {

    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private List<JobApplication> applicationList = new ArrayList<>();
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_courses);

        sessionManager = SessionManager.getInstance(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        recyclerView = findViewById(R.id.recyclerApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ApplicationAdapter(applicationList);
        recyclerView.setAdapter(adapter);

        setupDrawer();

        String token = sessionManager.getActiveToken();
        int userId = sessionManager.getUserId();
        if (token != null && !token.isEmpty() && userId > 0) {
            fetchAppliedJobs(token, userId);
        } else {
            Toast.makeText(this, "Please log in again.", Toast.LENGTH_SHORT).show();
        }

       // setOnClickListener(v -> startActivity(new Intent(Courses.this, Login.class)));
    }

        // ✅ Fetch applied jobs from API: /api/user/job/applied/{userId}
        private void fetchAppliedJobs(String token, int userId) {
            Call<JsonObject> call = apiService.getAppliedJobs("Bearer " + token, userId);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JsonObject jsonResponse = response.body();
                        JsonArray jobsArray = jsonResponse.getAsJsonArray("jobs");

                        applicationList.clear();
                        Gson gson = new Gson();

                        for (JsonElement jobElement : jobsArray) {
                            AppliedJob job = gson.fromJson(jobElement, AppliedJob.class);

                            JobApplication app = new JobApplication();
                            app.setJob_id(job.getJob_id());
                            app.setJob_title(safeText(job.getJob_title()));
                            app.setCompanyName(safeText(job.getCompany_name()));
                            app.setLocation(safeText(job.getLocation()));
                            app.setStatus(safeText(job.getStatus()));
                            app.setApplied_at(safeText(job.getApply_date()));

                            applicationList.add(app);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Courses.this, "Failed to load applied jobs", Toast.LENGTH_SHORT).show();
                        Log.e("Profile", "Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(Courses.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // ✅ Safe text for null handling
        private String safeText(String text) {
            return text != null && !text.trim().isEmpty() ? text : "N/A";
        }

    }
