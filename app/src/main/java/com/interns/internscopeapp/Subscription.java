//package com.interns.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class Subscription extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ApiService apiService;
//    private SessionManager sessionManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_subscription);
//
//        recyclerView = findViewById(R.id.rvPlans);
//        recyclerView.setLayoutManager(
//                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        );
//
//        apiService = ApiClient.getClient(this).create(ApiService.class);
//        sessionManager = SessionManager.getInstance(this);
//
//        String userType = sessionManager.getUserType();
//
//// Debug log to confirm what we actually got
//        Log.d("API_PLAN_DEBUG", "Logged-in user type: " + userType);
//
//// Now call API or filter plan list
//        // fetchPlans(userType);
//
//        fetchPlans();
//    }
//
//    private void fetchPlans() {
//        apiService.getPlans().enqueue(new Callback<List<Plan>>() {
//            @Override
//            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Plan> plans = response.body();
//
//                    SessionManager sessionManager = new SessionManager(Subscription.this);
//                    String userType = sessionManager.getUserType();
//
//                    Log.d("API_PLAN_DEBUG", "Logged-in user type: " + userType);
//
//                    List<Plan> filteredPlans;
//
//                    if ("user".equalsIgnoreCase(userType)) {
//                        // For company role, only show plan with ID = 2
//                        filteredPlans = new java.util.ArrayList<>();
//                        for (Plan plan : plans) {
//                            if (plan.getId() == 2) {
//                                filteredPlans.add(plan);
//                                break;
//                            }
//                        }
//                    } else {
//                        // For normal user, show user-type plans
//                        filteredPlans = new java.util.ArrayList<>();
//                        for (Plan plan : plans) {
//                            if ("company".equalsIgnoreCase(plan.getUserType())) {
//                                filteredPlans.add(plan);
//                            }
//
//                        }
//                    }
//
//                    if (!filteredPlans.isEmpty()) {
//                        PlanAdapter adapter = new PlanAdapter(Subscription.this, filteredPlans, plan -> {
//                            String token = "Bearer " + sessionManager.getActiveToken();
//                            int userId = sessionManager.getUserId();
//
//                            AddCartRequest request = new AddCartRequest(userId, plan.getId(), 1);
//
//                            // ✅ Add user plan to user cart
//                            Call<ApiResponse> callApi = apiService.addToCart(token, request);
//
//                            callApi.enqueue(new Callback<ApiResponse>() {
//                                @Override
//                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                                    if (response.isSuccessful() && response.body() != null) {
//                                        Toast.makeText(Subscription.this, "Plan added to cart!", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(Subscription.this, CartActivity.class));
//                                    } else {
//                                        Toast.makeText(Subscription.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
//                                        Log.e("API_PLAN_DEBUG", "Add to cart failed: " + response.code());
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ApiResponse> call, Throwable t) {
//                                    Toast.makeText(Subscription.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    Log.e("API_PLAN_DEBUG", "API error: " + t.getMessage());
//                                }
//                            });
//                        });
//
//                        recyclerView.setAdapter(adapter);
//
//                    } else {
//                        Toast.makeText(Subscription.this, "No user plans available.", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(Subscription.this, "Failed to fetch plans", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Plan>> call, Throwable t) {
//                Toast.makeText(Subscription.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("API_PLAN_DEBUG", "Fetch plans error: " + t.getMessage());
//            }
//        });
//        }
//    }

package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Subscription extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        recyclerView = findViewById(R.id.rvPlans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getClient(this).create(ApiService.class);
        sessionManager = SessionManager.getInstance(this);

        fetchPlans();
    }

    private void fetchPlans() {
        apiService.getPlans().enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(Subscription.this, "Failed to fetch plans", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Plan> plans = response.body();
                String userType = sessionManager.getUserType();

                List<Plan> filteredPlans = new java.util.ArrayList<>();

                if (userType.equalsIgnoreCase("user")) {
                    for (Plan p : plans) {
                        if (p.getId() == 2) filteredPlans.add(p);
                    }
                } else {
                    for (Plan p : plans) {
                        if (p.getUserType().equalsIgnoreCase("company")) filteredPlans.add(p);
                    }
                }

                PlanAdapter adapter = new PlanAdapter(Subscription.this, filteredPlans, plan -> {

                    String token = "Bearer " + sessionManager.getActiveToken();
                    int userId = sessionManager.getUserId();

                    // 🔥 Check if already in cart
                    apiService.getUserCart(token).enqueue(new Callback<CartResponse>() {
                        @Override
                        public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {

                            if (response.isSuccessful() && response.body() != null) {
                                for (CartItem c : response.body().getCart()) {
                                    if (c.getPlanId() == plan.getId()) {
                                        Toast.makeText(Subscription.this, "Plan already in cart!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Subscription.this, CartActivity.class));
                                        return; // stop
                                    }
                                }
                            }

                            // Not in cart → add 1 quantity
                            AddCartRequest request = new AddCartRequest(userId, plan.getId(), 1);

                            apiService.addToCart(token, request).enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    Toast.makeText(Subscription.this, "Added to cart!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Subscription.this, CartActivity.class));
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Toast.makeText(Subscription.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<CartResponse> call, Throwable t) {}
                    });
                });

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                Toast.makeText(Subscription.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
