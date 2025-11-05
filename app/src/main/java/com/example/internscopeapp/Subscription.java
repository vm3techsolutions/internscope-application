package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        recyclerView = findViewById(R.id.rvPlans);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        apiService = ApiClient.getClient(this).create(ApiService.class);

        fetchPlans();
    }

    private void fetchPlans() {
        apiService.getPlans().enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Plan> plans = response.body();

                    PlanAdapter adapter = new PlanAdapter(Subscription.this, plans, plan -> {
                        // Handle Buy button click
                        Intent intent = new Intent(Subscription.this, CheckoutActivity.class);
                        intent.putExtra("plan_id", plan.getId());
                        intent.putExtra("plan_name", plan.getName());
                        intent.putExtra("plan_price", plan.getPrice());
                        intent.putExtra("plan_duration", plan.getDurationDays());
                        intent.putExtra("plan_description", plan.getDescription());
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Subscription.this, "Failed to load plans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Subscription.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
