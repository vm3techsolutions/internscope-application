//package com.example.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.navigation.NavigationView;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class Home extends AppCompatActivity {
//
//    private DrawerLayout drawerLayout;
//    private Button signin, viewjob, viewjob2;
//    private SessionManager sessionManager;
//    private String currentUsername;
//    private ImageView profileImg;
//    private RecyclerView recyclerView;
//    private ProgressBar progressBar;
//    private JobsAdapter jobAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//
//
//        // Initialize SessionManager first
//        sessionManager = new SessionManager(this);
//        recyclerView = findViewById(R.id.rvJobs);
//        progressBar = findViewById(R.id.progressBar);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        RecyclerView rvJobs = findViewById(R.id.rvJobs);
//        rvJobs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        rvJobs.setAdapter(jobAdapter);
//
//
//        // Check if user is logged in
//        if (!sessionManager.isLoggedIn()) {
//            // If not logged in, redirect to Login activity
//            startActivity(new Intent(Home.this, Login.class));
//            finish();
//            return;
//        }
//
//        // Get current username from session
//        currentUsername = sessionManager.getUsername();
//
//        fetchAllJobs();
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//
//        setSupportActionBar(toolbar);
//
//        // Drawer toggle
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        // Buttons
//        signin = findViewById(R.id.signinbtn);
//        viewjob = findViewById(R.id.viewjob);
//        viewjob2 = findViewById(R.id.viewjob2);
//
//        // Button clicks
//        if (signin != null) {
//            signin.setOnClickListener(v -> startActivity(new Intent(Home.this, Packages.class)));
//        }
//
//        if (viewjob != null) {
//            viewjob.setOnClickListener(v -> startActivity(new Intent(Home.this, Jobs.class)));
//        }
//
//        if (viewjob2 != null) {
//            viewjob2.setOnClickListener(v -> startActivity(new Intent(Home.this, Courses.class)));
//        }
//
//        // Navigation header
//        if (navigationView != null) {
//            profileImg = navigationView.getHeaderView(0).findViewById(R.id.nav_profile_img);
//            TextView usernameTextView = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
//
//            if (usernameTextView != null) usernameTextView.setText(currentUsername);
//            if (profileImg != null) profileImg.setImageResource(R.drawable.user); // default image
//
//            navigationView.setNavigationItemSelectedListener(item -> {
//                int id = item.getItemId();
//
//                if (id == R.id.nav_profile) {
//                    startActivity(new Intent(Home.this, Profile.class));
//                } else if (id == R.id.nav_searchjob) {
//                    startActivity(new Intent(Home.this, Jobs.class));
//                } else if (id == R.id.nav_training) {
//                    startActivity(new Intent(Home.this, Courses.class));
//                } else if (id == R.id.nav_subscription) {
//                    startActivity(new Intent(Home.this, Subscription.class));
//                } else if (id == R.id.nav_package) {
//                    startActivity(new Intent(Home.this, Packages.class));
//                } else if (id == R.id.nav_recomend) {
//                    startActivity(new Intent(Home.this, Opportunity.class));
//                }
//
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            });
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
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
//                    jobAdapter = new JobsAdapter(Home.this, jobList);
//                    recyclerView.setAdapter(jobAdapter);
//
//                } else {
//                    Toast.makeText(Home.this, "No jobs found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<JobResponse>> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(Home.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("API_ERROR", t.getMessage());
//            }
//        });
//    }
//}

package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Button signin, viewjob, viewjob2;
    private SessionManager sessionManager;
    private String currentUsername, userType;
    private ImageView profileImg;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JobsAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize session and layout elements
        sessionManager = new SessionManager(this);
        recyclerView = findViewById(R.id.rvJobs);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Redirect to login if user not logged in
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(Home.this, Login.class));
            finish();
            return;
        }

        // Get current user info
        currentUsername = sessionManager.getUsername();
        userType = sessionManager.getUserType(); // stored during login (e.g. "student" or "company")

        fetchAllJobs();

        // Drawer setup
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Button references
        signin = findViewById(R.id.signinbtn);
        viewjob = findViewById(R.id.viewjob);
        viewjob2 = findViewById(R.id.viewjob2);

        // Button click listeners
        if (signin != null)
            signin.setOnClickListener(v -> startActivity(new Intent(Home.this, Packages.class)));

        if (viewjob != null)
            viewjob.setOnClickListener(v -> startActivity(new Intent(Home.this, Jobs.class)));

        if (viewjob2 != null)
            viewjob2.setOnClickListener(v -> startActivity(new Intent(Home.this, Courses.class)));

        // Navigation header + menu setup
        if (navigationView != null) {
            profileImg = navigationView.getHeaderView(0).findViewById(R.id.nav_profile_img);
            TextView usernameTextView = navigationView.getHeaderView(0).findViewById(R.id.nav_username);

            if (usernameTextView != null) usernameTextView.setText(currentUsername);
            if (profileImg != null) profileImg.setImageResource(R.drawable.user);

            // ✅ Dynamic Navigation Drawer logic
            Menu menu = navigationView.getMenu();
            if (userType != null) {
                if (userType.equalsIgnoreCase("user")) {
                    // Student view
                    menu.findItem(R.id.nav_dashboard).setVisible(true);
                    menu.findItem(R.id.nav_companydashboard).setVisible(false);
                    menu.findItem(R.id.nav_profile).setVisible(true);
                    menu.findItem(R.id.nav_comapny_profile).setVisible(false);
                    menu.findItem(R.id.nav_searchjob).setVisible(true);
                    menu.findItem(R.id.nav_companyjob).setVisible(false);
                    menu.findItem(R.id.nav_training).setVisible(true);
                    menu.findItem(R.id.nav_Applications).setVisible(false);
                    menu.findItem(R.id.nav_subscription).setVisible(true);
                    menu.findItem(R.id.nav_package).setVisible(false);
                    menu.findItem(R.id.nav_recomend).setVisible(false);
                } else if (userType.equalsIgnoreCase("company")) {
                    // Company view
                    menu.findItem(R.id.nav_dashboard).setVisible(false);
                    menu.findItem(R.id.nav_companydashboard).setVisible(true);
                    menu.findItem(R.id.nav_profile).setVisible(false);
                    menu.findItem(R.id.nav_comapny_profile).setVisible(true);
                    menu.findItem(R.id.nav_searchjob).setVisible(false);
                    menu.findItem(R.id.nav_companyjob).setVisible(true);
                    menu.findItem(R.id.nav_training).setVisible(false);
                    menu.findItem(R.id.nav_Applications).setVisible(true);
                    menu.findItem(R.id.nav_subscription).setVisible(false);
                    menu.findItem(R.id.nav_package).setVisible(true);
                    menu.findItem(R.id.nav_recomend).setVisible(false);
                }
            }

            // Common items
            menu.findItem(R.id.nav_logout).setVisible(true);

            // ✅ Navigation click actions
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if(id == R.id.nav_dashboard){
                    startActivity(new Intent(Home.this, UserDashboard.class));
                }else if(id == R.id.nav_companydashboard) {
                    startActivity(new Intent(Home.this, CompanyDashboard.class));
                }else if (id == R.id.nav_profile) {
                    startActivity(new Intent(Home.this, Profile.class));
                } else if (id == R.id.nav_comapny_profile) {
                    startActivity(new Intent(Home.this, CompanyProfile.class));
                } else if (id == R.id.nav_searchjob) {
                    startActivity(new Intent(Home.this, Jobs.class));
                } else if (id == R.id.nav_companyjob) {
                    startActivity(new Intent(Home.this, Jobs.class));
                } else if (id == R.id.nav_training) {
                    startActivity(new Intent(Home.this, Courses.class));
                } else if (id == R.id.nav_subscription) {
                    startActivity(new Intent(Home.this, Subscription.class));
                } else if (id == R.id.nav_package) {
                    startActivity(new Intent(Home.this, Packages.class));
                } else if (id == R.id.nav_recomend) {
                    startActivity(new Intent(Home.this, Opportunity.class));
                } else if (id == R.id.nav_logout) {
                    sessionManager.logout();
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                    jobAdapter = new JobsAdapter(Home.this, jobList);
                    recyclerView.setAdapter(jobAdapter);
                } else {
                    Toast.makeText(Home.this, "No jobs found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<JobResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Home.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
}

