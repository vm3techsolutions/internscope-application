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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator3;
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

        //slider image

        RecyclerView sliderRecycler = findViewById(R.id.sliderRecycler);

        List<Integer> images = Arrays.asList(
                //R.drawable.frame2,
                R.drawable.frame3,
                R.drawable.frame4
                //R.drawable.frame1
        );

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        sliderRecycler.setLayoutManager(layoutManager);
        SafeSliderAdapter adapter = new SafeSliderAdapter(this, images);
        sliderRecycler.setAdapter(adapter);

// Snap to center
        //SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(sliderRecycler);

// Auto-scroll safely using a Timer (NOT UI thread)
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    int nextPos = layoutManager.findFirstVisibleItemPosition() + 1;
                    if (nextPos >= images.size()) nextPos = 0;
                    sliderRecycler.smoothScrollToPosition(nextPos);
                });
            }
        }, 3000, 3000);



        // ---------- Categories ----------
        setupCategories();

        // ---------- Drawer Header ----------
        if (navigationView != null) {
            View header = navigationView.getHeaderView(0);
            profileImg = header.findViewById(R.id.nav_profile_img);
            TextView usernameTextView = header.findViewById(R.id.nav_username);

            usernameTextView.setText(currentUsername);
            profileImg.setImageResource(R.drawable.user_94);

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

//        else if (id == R.id.nav_logout) {
//            sessionManager.logout();
//            Intent intent = new Intent(this, Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }

        else if (id == R.id.nav_logout) {

            sessionManager.logout();

            Intent intent;

            if ("user".equalsIgnoreCase(userType)) {
                // Candidate login
                intent = new Intent(this, Login.class);
            } else {
                // Company login
                intent = new Intent(this, Company_login.class);
            }

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


