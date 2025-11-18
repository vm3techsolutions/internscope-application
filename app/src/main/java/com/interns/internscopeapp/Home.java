////package com.interns.internscopeapp;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.util.Log;
////import android.view.View;
////import android.widget.Button;
////import android.widget.ImageView;
////import android.widget.ProgressBar;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import androidx.appcompat.app.ActionBarDrawerToggle;
////import androidx.appcompat.widget.Toolbar;
////import androidx.core.view.GravityCompat;
////import androidx.drawerlayout.widget.DrawerLayout;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.google.android.material.navigation.NavigationView;
////
////import java.util.List;
////
////import retrofit2.Call;
////import retrofit2.Callback;
////import retrofit2.Response;
////
////public class Home extends AppCompatActivity {
////
////    private DrawerLayout drawerLayout;
////    private Button signin, viewjob, viewjob2;
////    private SessionManager sessionManager;
////    private String currentUsername;
////    private ImageView profileImg;
////    private RecyclerView recyclerView;
////    private ProgressBar progressBar;
////    private JobsAdapter jobAdapter;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_home);
////
////
////
////        // Initialize SessionManager first
////        sessionManager = new SessionManager(this);
////        recyclerView = findViewById(R.id.rvJobs);
////        progressBar = findViewById(R.id.progressBar);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////
////        RecyclerView rvJobs = findViewById(R.id.rvJobs);
////        rvJobs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
////        rvJobs.setAdapter(jobAdapter);
////
////
////        // Check if user is logged in
////        if (!sessionManager.isLoggedIn()) {
////            // If not logged in, redirect to Login activity
////            startActivity(new Intent(Home.this, Login.class));
////            finish();
////            return;
////        }
////
////        // Get current username from session
////        currentUsername = sessionManager.getUsername();
////
////        fetchAllJobs();
////
////        drawerLayout = findViewById(R.id.drawer_layout);
////        Toolbar toolbar = findViewById(R.id.toolbar);
////        NavigationView navigationView = findViewById(R.id.nav_view);
////
////        setSupportActionBar(toolbar);
////
////        // Drawer toggle
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
////                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        drawerLayout.addDrawerListener(toggle);
////        toggle.syncState();
////
////        // Buttons
////        signin = findViewById(R.id.signinbtn);
////        viewjob = findViewById(R.id.viewjob);
////        viewjob2 = findViewById(R.id.viewjob2);
////
////        // Button clicks
////        if (signin != null) {
////            signin.setOnClickListener(v -> startActivity(new Intent(Home.this, Packages.class)));
////        }
////
////        if (viewjob != null) {
////            viewjob.setOnClickListener(v -> startActivity(new Intent(Home.this, Jobs.class)));
////        }
////
////        if (viewjob2 != null) {
////            viewjob2.setOnClickListener(v -> startActivity(new Intent(Home.this, Courses.class)));
////        }
////
////        // Navigation header
////        if (navigationView != null) {
////            profileImg = navigationView.getHeaderView(0).findViewById(R.id.nav_profile_img);
////            TextView usernameTextView = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
////
////            if (usernameTextView != null) usernameTextView.setText(currentUsername);
////            if (profileImg != null) profileImg.setImageResource(R.drawable.user); // default image
////
////            navigationView.setNavigationItemSelectedListener(item -> {
////                int id = item.getItemId();
////
////                if (id == R.id.nav_profile) {
////                    startActivity(new Intent(Home.this, Profile.class));
////                } else if (id == R.id.nav_searchjob) {
////                    startActivity(new Intent(Home.this, Jobs.class));
////                } else if (id == R.id.nav_training) {
////                    startActivity(new Intent(Home.this, Courses.class));
////                } else if (id == R.id.nav_subscription) {
////                    startActivity(new Intent(Home.this, Subscription.class));
////                } else if (id == R.id.nav_package) {
////                    startActivity(new Intent(Home.this, Packages.class));
////                } else if (id == R.id.nav_recomend) {
////                    startActivity(new Intent(Home.this, Opportunity.class));
////                }
////
////                drawerLayout.closeDrawer(GravityCompat.START);
////                return true;
////            });
////        }
////    }
////
////    @Override
////    public void onBackPressed() {
////        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
////            drawerLayout.closeDrawer(GravityCompat.START);
////        } else {
////            super.onBackPressed();
////        }
////    }
////
////    private void fetchAllJobs() {
////        progressBar.setVisibility(View.VISIBLE);
////
////        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
////        Call<List<JobResponse>> call = apiService.getAllJobs();
////
////        call.enqueue(new Callback<List<JobResponse>>() {
////            @Override
////            public void onResponse(Call<List<JobResponse>> call, Response<List<JobResponse>> response) {
////                progressBar.setVisibility(View.GONE);
////                if (response.isSuccessful() && response.body() != null) {
////                    List<JobResponse> jobList = response.body();
////
////                    // Updated adapter call
////                    jobAdapter = new JobsAdapter(Home.this, jobList);
////                    recyclerView.setAdapter(jobAdapter);
////
////                } else {
////                    Toast.makeText(Home.this, "No jobs found", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<List<JobResponse>> call, Throwable t) {
////                progressBar.setVisibility(View.GONE);
////                Toast.makeText(Home.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
////                Log.e("API_ERROR", t.getMessage());
////            }
////        });
////    }
////}
//
//package com.interns.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
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
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
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
//    private String currentUsername, userType;
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
//        // Initialize session and layout elements
//        sessionManager = SessionManager.getInstance(this);
//        recyclerView = findViewById(R.id.rvJobs);
//        progressBar = findViewById(R.id.progressBar);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        RecyclerView rvTestimonials = findViewById(R.id.rvTestimonials);
//        // Redirect to login if user not logged in
//        if (!sessionManager.isLoggedIn()) {
//            startActivity(new Intent(Home.this, Login.class));
//            finish();
//            return;
//        }
//
//        // Get current user info
//        currentUsername = sessionManager.getUsername();
//        userType = sessionManager.getUserType(); // stored during login (e.g. "student" or "company")
//
//        fetchAllJobs();
//
//        // Drawer setup
//        drawerLayout = findViewById(R.id.drawer_layout);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        setSupportActionBar(toolbar);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        setupCategories();
//
//
//        rvTestimonials.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//
//        List<TestimonialModel> testimonialList = loadTestimonials();
//        TestimonialAdapter testimonialAdapter = new TestimonialAdapter(this, testimonialList);
//        rvTestimonials.setAdapter(testimonialAdapter);
//
//        private List<TestimonialModel> loadTestimonials() {
//
//            List<TestimonialModel> list = new ArrayList<>();
//
//            try {
//                InputStream is = getAssets().open("testimonials.json");
//                int size = is.available();
//                byte[] buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//
//                String json = new String(buffer, StandardCharsets.UTF_8);
//
//                JSONArray arr = new JSONArray(json);
//
//                for (int i = 0; i < arr.length(); i++) {
//
//                    JSONObject obj = arr.getJSONObject(i);
//                    TestimonialModel m = new TestimonialModel();
//
//                    m.id = obj.getInt("id");
//                    m.image = obj.getString("image");
//                    m.feedback1 = obj.getString("feedback1");
//                    m.author = obj.getString("author");
//                    m.role = obj.getString("role");
//                    m.rating = obj.getDouble("rating");
//
//                    list.add(m);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return list;
//        }
//
//
//
//
//        // Navigation header + menu setup
//        if (navigationView != null) {
//            profileImg = navigationView.getHeaderView(0).findViewById(R.id.nav_profile_img);
//            TextView usernameTextView = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
//
//            if (usernameTextView != null) usernameTextView.setText(currentUsername);
//            if (profileImg != null) profileImg.setImageResource(R.drawable.user);
//
//            // ✅ Dynamic Navigation Drawer logic
//            Menu menu = navigationView.getMenu();
//            if (userType != null) {
//                if (userType.equalsIgnoreCase("user")) {
//                    // Student view
//                    menu.findItem(R.id.nav_dashboard).setVisible(true);
//                    menu.findItem(R.id.nav_companydashboard).setVisible(false);
//                    menu.findItem(R.id.nav_profile).setVisible(true);
//                    menu.findItem(R.id.nav_comapny_profile).setVisible(false);
//                    menu.findItem(R.id.nav_searchjob).setVisible(true);
//                    menu.findItem(R.id.nav_companyjob).setVisible(false);
//                    menu.findItem(R.id.nav_training).setVisible(true);
//                    menu.findItem(R.id.nav_Applications).setVisible(false);
//                    menu.findItem(R.id.nav_subscription).setVisible(true);
//                    menu.findItem(R.id.nav_package).setVisible(false);
//                    menu.findItem(R.id.nav_recomend).setVisible(false);
//                } else if (userType.equalsIgnoreCase("company")) {
//                    // Company view
//                    menu.findItem(R.id.nav_dashboard).setVisible(false);
//                    menu.findItem(R.id.nav_companydashboard).setVisible(true);
//                    menu.findItem(R.id.nav_profile).setVisible(false);
//                    menu.findItem(R.id.nav_comapny_profile).setVisible(true);
//                    menu.findItem(R.id.nav_searchjob).setVisible(false);
//                    menu.findItem(R.id.nav_companyjob).setVisible(true);
//                    menu.findItem(R.id.nav_training).setVisible(false);
//                    menu.findItem(R.id.nav_Applications).setVisible(true);
//                    menu.findItem(R.id.nav_subscription).setVisible(false);
//                    menu.findItem(R.id.nav_package).setVisible(true);
//                    menu.findItem(R.id.nav_recomend).setVisible(false);
//                }
//            }
//
//            // Common items
//            menu.findItem(R.id.nav_logout).setVisible(true);
//
//            // ✅ Navigation click actions
//            navigationView.setNavigationItemSelectedListener(item -> {
//                int id = item.getItemId();
//                if(id == R.id.nav_dashboard){
//                    startActivity(new Intent(Home.this, UserDashboard.class));
//                }else if(id == R.id.nav_companydashboard) {
//                    startActivity(new Intent(Home.this, CompanyDashboard.class));
//                }else if (id == R.id.nav_profile) {
//                    startActivity(new Intent(Home.this, Profile.class));
//                } else if (id == R.id.nav_comapny_profile) {
//                    startActivity(new Intent(Home.this, CompanyProfile.class));
//                } else if (id == R.id.nav_searchjob) {
//                    startActivity(new Intent(Home.this, Jobs.class));
//                } else if (id == R.id.nav_companyjob) {
//                    startActivity(new Intent(Home.this, Jobs.class));
//                } else if (id == R.id.nav_training) {
//                    startActivity(new Intent(Home.this, Courses.class));
//                } else if (id == R.id.nav_subscription) {
//                    startActivity(new Intent(Home.this, Subscription.class));
//                } else if (id == R.id.nav_package) {
//                    startActivity(new Intent(Home.this, Packages.class));
//                } else if (id == R.id.nav_Applications) {
//                    startActivity(new Intent(Home.this, Opportunity.class));
//                } else if (id == R.id.nav_logout) {
//                    sessionManager.logout();
//
//                    // Redirect user based on type
//                    Intent intent;
//                    if (userType != null) {
//                        if (userType.equalsIgnoreCase("user")) {
//                            intent = new Intent(Home.this, Login.class); // Student login
//                        } else if (userType.equalsIgnoreCase("company")) {
//                            intent = new Intent(Home.this, Company_login.class); // Company login
//                        } else {
//                            intent = new Intent(Home.this, Login.class); // fallback
//                        }
//                    } else {
//                        intent = new Intent(Home.this, Login.class); // default fallback
//                    }
//
//                    // Clear previous activities from stack
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
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
////    private void fetchAllJobs() {
////        progressBar.setVisibility(View.VISIBLE);
////
////        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
////        Call<List<JobResponse>> call = apiService.getAllJobs();
////
////        call.enqueue(new Callback<List<JobResponse>>() {
////            @Override
////            public void onResponse(Call<List<JobResponse>> call, Response<List<JobResponse>> response) {
////                progressBar.setVisibility(View.GONE);
////                if (response.isSuccessful() && response.body() != null) {
////                    List<JobResponse> jobList = response.body();
////                    jobAdapter = new JobsAdapter(Home.this, jobList);
////                    recyclerView.setAdapter(jobAdapter);
////                } else {
////                    Toast.makeText(Home.this, "No jobs found", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<List<JobResponse>> call, Throwable t) {
////                progressBar.setVisibility(View.GONE);
////                Toast.makeText(Home.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
////                Log.e("API_ERROR", t.getMessage());
////            }
////        });
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
//                    // Ignore job_tag without touching adapter or response class
//                    for (JobResponse job : jobList) {
//                        job.setJobTag(null); // Clear the tags
//                    }
//
//                    jobAdapter = new JobsAdapter(Home.this, jobList);
//                    recyclerView.setAdapter(jobAdapter);
//                } else {
//                    Toast.makeText(Home.this, "No jobs found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<JobResponse>> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(Home.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//        private void setupCategories() {
//            setCategory(R.id.c1, R.drawable.technology, "1", "Information Technology");
//            setCategory(R.id.c2, R.drawable.manufacturing, "2", "Manufacturing");
//            setCategory(R.id.c3, R.drawable.medical, "3", "Healthcare/Medical");
//            setCategory(R.id.c4, R.drawable.finance, "4", "Finance/Insurance");
//            setCategory(R.id.c5, R.drawable.education, "5", "Education/EdTech");
//            setCategory(R.id.c6, R.drawable.insurance, "6", "Retail/Ecommerce");
//            setCategory(R.id.c7, R.drawable.technology, "7", "Telecommunication");
//            setCategory(R.id.c8, R.drawable.tourism, "8", "Travel/Tourism");
//            setCategory(R.id.c9, R.drawable.infrastructure, "9", "Infrastructure");
//            setCategory(R.id.c10, R.drawable.transportation, "10", "Logistics/Transport");
//        }
//
//        private void setCategory( int id, int icon, String number, String title){
//            View v = findViewById(id);
//            ((ImageView) v.findViewById(R.id.icon)).setImageResource(icon);
//            ((TextView) v.findViewById(R.id.catNumber)).setText(number);
//            ((TextView) v.findViewById(R.id.title)).setText(title);
//        }
//
//    }
//
//
//
//

package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private RecyclerView rvTestimonials;

    private ProgressBar progressBar;
    private JobsAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Session
        sessionManager = SessionManager.getInstance(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        // Drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Redirect if not logged in
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(Home.this, Login.class));
            finish();
            return;
        }

        currentUsername = sessionManager.getUsername();
        userType = sessionManager.getUserType();

        // Jobs RecyclerView (Horizontal)
        recyclerView = findViewById(R.id.rvJobs);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        fetchAllJobs();

        // ---------- Testimonials RecyclerView ----------
        rvTestimonials = findViewById(R.id.rvTestimonials);
        rvTestimonials.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<TestimonialModel> testimonialList = loadTestimonials();
        TestimonialAdapter testimonialAdapter = new TestimonialAdapter(this, testimonialList);
        rvTestimonials.setAdapter(testimonialAdapter);

        // Snap (swipe animation)
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvTestimonials);

// Auto-scroll every 3 sec
        final Handler handler = new Handler();
        final int scrollDelay = 3000;

        Runnable runnable = new Runnable() {
            int position = 0;
            @Override
            public void run() {
                if (testimonialAdapter.getItemCount() == 0) return;

                if (position >= testimonialAdapter.getItemCount()) {
                    position = 0;
                }

                rvTestimonials.smoothScrollToPosition(position++);
                handler.postDelayed(this, scrollDelay);
            }
        };

        handler.postDelayed(runnable, scrollDelay);

        // ---------- Categories ----------
        setupCategories();

        // ---------- Drawer Header ----------
        if (navigationView != null) {
            View header = navigationView.getHeaderView(0);
            profileImg = header.findViewById(R.id.nav_profile_img);
            TextView usernameTextView = header.findViewById(R.id.nav_username);

            usernameTextView.setText(currentUsername);
            profileImg.setImageResource(R.drawable.user);

            updateDrawerMenu(navigationView.getMenu());

            navigationView.setNavigationItemSelectedListener(item -> {
                handleNavigationClick(item.getItemId());
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }

        // Buttons
//        signin = findViewById(R.id.signinbtn);
      viewjob = findViewById(R.id.viewjob);
//        viewjob2 = findViewById(R.id.viewjob2);
//
//        if (signin != null) signin.setOnClickListener(v -> startActivity(new Intent(Home.this, Packages.class)));
        if (viewjob != null) viewjob.setOnClickListener(v -> startActivity(new Intent(Home.this, Jobs.class)));
//        if (viewjob2 != null) viewjob2.setOnClickListener(v -> startActivity(new Intent(Home.this, Courses.class)));
    }

    // ===========================================
    //      FETCH JOBS API
    // ===========================================
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

                    for (JobResponse job : jobList) {
                        job.setJobTag(null);
                    }

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
            }
        });
    }

    // ===========================================
    //      LOAD TESTIMONIALS FROM ASSETS
    // ===========================================
    private List<TestimonialModel> loadTestimonials() {

        List<TestimonialModel> list = new ArrayList<>();

        try {
            InputStream is = getAssets().open("testimonials.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++) {

                JSONObject obj = arr.getJSONObject(i);
                TestimonialModel m = new TestimonialModel();

                m.id = obj.getInt("id");
                m.image = obj.getString("image");
                m.feedback1 = obj.getString("feedback1");
                m.author = obj.getString("author");
                m.role = obj.getString("role");
                m.rating = obj.getDouble("rating");

                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===========================================
    //      CATEGORIES
    // ===========================================
    private void setupCategories() {
        setCategory(R.id.c1, R.drawable.technology, "1", "Information Technology");
        setCategory(R.id.c2, R.drawable.manufacturing, "2", "Manufacturing");
        setCategory(R.id.c3, R.drawable.medical, "3", "Healthcare/Medical");
        setCategory(R.id.c4, R.drawable.finance, "4", "Finance/Insurance");
        setCategory(R.id.c5, R.drawable.education, "5", "Education/EdTech");
        setCategory(R.id.c6, R.drawable.insurance, "6", "Retail/Ecommerce");
        setCategory(R.id.c7, R.drawable.technology, "7", "Telecommunication");
        setCategory(R.id.c8, R.drawable.tourism, "8", "Travel/Tourism");
        setCategory(R.id.c9, R.drawable.infrastructure, "9", "Infrastructure");
        setCategory(R.id.c10, R.drawable.transportation, "10", "Logistics/Transport");
    }

    private void setCategory(int id, int icon, String number, String title) {
        View v = findViewById(id);
        ((ImageView) v.findViewById(R.id.icon)).setImageResource(icon);
        ((TextView) v.findViewById(R.id.catNumber)).setText(number);
        ((TextView) v.findViewById(R.id.title)).setText(title);
    }

    // ===========================================
    //      DRAWER MENU VISIBILITY BASED ON USER TYPE
    // ===========================================
    private void updateDrawerMenu(Menu menu) {

        if (userType == null) return;

        boolean isUser = userType.equalsIgnoreCase("user");

        menu.findItem(R.id.nav_dashboard).setVisible(isUser);
        menu.findItem(R.id.nav_companydashboard).setVisible(!isUser);

        menu.findItem(R.id.nav_profile).setVisible(isUser);
        menu.findItem(R.id.nav_comapny_profile).setVisible(!isUser);

        menu.findItem(R.id.nav_searchjob).setVisible(isUser);
        menu.findItem(R.id.nav_companyjob).setVisible(!isUser);

        menu.findItem(R.id.nav_training).setVisible(isUser);
        menu.findItem(R.id.nav_Applications).setVisible(!isUser);

        menu.findItem(R.id.nav_subscription).setVisible(isUser);
        menu.findItem(R.id.nav_package).setVisible(!isUser);

        menu.findItem(R.id.nav_logout).setVisible(true);
    }

    // ===========================================
    //      DRAWER ITEM CLICK HANDLING
    // ===========================================
    private void handleNavigationClick(int id) {

        if (id == R.id.nav_dashboard)
            startActivity(new Intent(this, UserDashboard.class));

        else if (id == R.id.nav_companydashboard)
            startActivity(new Intent(this, CompanyDashboard.class));

        else if (id == R.id.nav_profile)
            startActivity(new Intent(this, Profile.class));

        else if (id == R.id.nav_comapny_profile)
            startActivity(new Intent(this, CompanyProfile.class));

        else if (id == R.id.nav_searchjob || id == R.id.nav_companyjob)
            startActivity(new Intent(this, Jobs.class));

        else if (id == R.id.nav_training)
            startActivity(new Intent(this, Courses.class));

        else if (id == R.id.nav_subscription)
            startActivity(new Intent(this, Subscription.class));

        else if (id == R.id.nav_package)
            startActivity(new Intent(this, Packages.class));

        else if (id == R.id.nav_Applications)
            startActivity(new Intent(this, Opportunity.class));

        else if (id == R.id.nav_logout) {
            sessionManager.logout();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
