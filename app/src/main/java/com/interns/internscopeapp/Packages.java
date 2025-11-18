package com.interns.internscopeapp;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        apiService = ApiClient.getClient(this).create(ApiService.class);
        sessionManager = SessionManager.getInstance(this);

        String userType = sessionManager.getUserType();
        Log.d("API_PLAN_DEBUG", "Logged-in user type: " + userType);

        fetchPlans();
    }

    private void fetchPlans() {
        apiService.getPlans().enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Plan> plans = response.body();

                    String userType = sessionManager.getUserType();
                    Log.d("API_PLAN_DEBUG", "User type from session: " + userType);

                    List<Plan> filteredPlans = new ArrayList<>();

                    if ("company".equalsIgnoreCase(userType)) {
                        for (Plan plan : plans) {
                            if (plan.getId() == 3) { // show only employer plan
                                filteredPlans.add(plan);
                                break;
                            }
                        }
                    } else {
                        for (Plan plan : plans) {
                            if ("user".equalsIgnoreCase(plan.getUserType())) {
                                filteredPlans.add(plan);
                            }
                        }
                    }

                    if (!filteredPlans.isEmpty()) {
                        PlanAdapter adapter = new PlanAdapter(Packages.this, filteredPlans, plan -> {
                            String token = "Bearer " + sessionManager.getActiveToken();
                            int userId = sessionManager.getUserId();
                           // String userType = sessionManager.getUserType();

                            AddCartRequest request = new AddCartRequest(userId, plan.getId(), 1);

                            Call<ApiResponse> callApi;
                            if ("company".equalsIgnoreCase(userType)) {
                                callApi = apiService.addCompanyCart(token, request);
                            } else {
                                callApi = apiService.addToCart(token, request);
                            }

                            callApi.enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Toast.makeText(Packages.this, "Plan added to cart!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Packages.this, CartActivity.class));
                                    } else {
                                        Toast.makeText(Packages.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                                        Log.e("API_PLAN_DEBUG", "Add to cart failed: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Toast.makeText(Packages.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("API_PLAN_DEBUG", "API error: " + t.getMessage());
                                }
                            });
                        });

                        // ✅ Important: Attach adapter to RecyclerView
                        recyclerView.setAdapter(adapter);

                    } else {
                        Toast.makeText(Packages.this, "No plans available.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Packages.this, "Failed to fetch plans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                Toast.makeText(Packages.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_PLAN_DEBUG", "Fetch plans error: " + t.getMessage());
            }
        });
    }
}
