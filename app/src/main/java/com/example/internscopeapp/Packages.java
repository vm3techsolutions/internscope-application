package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Packages extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        recyclerView = findViewById(R.id.rvPlans);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        apiService = ApiClient.getClient(this).create(ApiService.class);
        sessionManager = SessionManager.getInstance(this);

        String userType = sessionManager.getUserType();

// Debug log to confirm what we actually got
        Log.d("API_PLAN_DEBUG", "Logged-in user type: " + userType);

// Now call API or filter plan list
       // fetchPlans(userType);

        fetchPlans();
    }

    private void fetchPlans() {
        apiService.getPlans().enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Plan> plans = response.body();

                    SessionManager sessionManager = new SessionManager(Packages.this);
                    String userType = sessionManager.getUserType();

                    Log.d("API_PLAN_DEBUG", "Logged-in user type: " + userType);

                    List<Plan> filteredPlans;

                    if ("company".equalsIgnoreCase(userType)) {
                        // For company role, only show plan with ID = 3
                        filteredPlans = new java.util.ArrayList<>();
                        for (Plan plan : plans) {
                            if (plan.getId() == 3) {
                                filteredPlans.add(plan);
                                break;
                            }
                        }
                    } else {
                        // For normal user, show user-type plans
                        filteredPlans = new java.util.ArrayList<>();
                        for (Plan plan : plans) {
                            if ("user".equalsIgnoreCase(plan.getUserType())) {
                                filteredPlans.add(plan);
                            }
                        }
                    }

                    if (!filteredPlans.isEmpty()) {
                        PlanAdapter adapter = new PlanAdapter(Packages.this, filteredPlans, plan -> {
                            Intent intent = new Intent(Packages.this, CheckoutActivity.class);
                            intent.putExtra("plan_id", plan.getId());
                            intent.putExtra("plan_name", plan.getName());
                            intent.putExtra("plan_price", plan.getPrice());
                            intent.putExtra("plan_duration", plan.getDurationDays());
                            intent.putExtra("plan_description", plan.getDescription());
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(Packages.this, "No plans available for this user type", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Packages.this, "Failed to load plans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Packages.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


//    private void fetchPlans() {
//        apiService.getPlans().enqueue(new Callback<List<Plan>>() {
//            @Override
//            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Plan> allPlans = response.body();
////                    for (Plan plan : allPlans) {
////                        android.util.Log.d("API_PLAN_DEBUG",
////                                "Plan: " + plan.getName() + " | Type: " + plan.getUserType());
////                    }
//
//                    // Get logged-in user type (user or employer)
//                    String userType = sessionManager.getUserType();
//                    if (userType == null || userType.isEmpty()) {
//                        Toast.makeText(Packages.this, "User type not found in session", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    // Filter plans based on user type
//                    List<Plan> filteredPlans = new ArrayList<>();
//                    for (Plan plan : allPlans) {
//                        if (plan.getUserType() != null &&
//                                plan.getUserType().equalsIgnoreCase(userType)) {
//                            filteredPlans.add(plan);
//                        }
//                    }
//
//                    if (filteredPlans.isEmpty()) {
//                        Toast.makeText(Packages.this, "No plans found for your account type", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    // Set filtered plans to adapter
//                    PlanAdapter adapter = new PlanAdapter(Packages.this, filteredPlans, plan -> {
//                        Intent intent = new Intent(Packages.this, CheckoutActivity.class);
//                        intent.putExtra("plan_id", plan.getId());
//                        intent.putExtra("plan_name", plan.getName());
//                        intent.putExtra("plan_price", plan.getPrice());
//                        intent.putExtra("plan_duration", plan.getDurationDays());
//                        intent.putExtra("plan_description", plan.getDescription());
//                        startActivity(intent);
//                    });
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    Toast.makeText(Packages.this, "Failed to load plans", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Plan>> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(Packages.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
