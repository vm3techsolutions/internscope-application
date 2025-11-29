package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;

    protected SessionManager sessionManager;
    protected String currentUsername, userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = SessionManager.getInstance(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        }

        currentUsername = sessionManager.getUsername();
        userType = sessionManager.getUserType();
    }

    protected void setupDrawer() {

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupHeader();
        setupMenuVisibility();
        handleMenuClicks();
    }

    private void setupHeader() {
        View header = navigationView.getHeaderView(0);
        TextView username = header.findViewById(R.id.nav_username);
        ImageView img = header.findViewById(R.id.nav_profile_img);

        username.setText(currentUsername);
        img.setImageResource(R.drawable.user_94);
    }

    private void setupMenuVisibility() {
        Menu menu = navigationView.getMenu();

        if ("user".equalsIgnoreCase(userType)) {
            menu.findItem(R.id.nav_dashboard).setVisible(true);
            menu.findItem(R.id.nav_companydashboard).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(true);
            menu.findItem(R.id.nav_comapny_profile).setVisible(false);
            menu.findItem(R.id.nav_searchjob).setVisible(true);
            menu.findItem(R.id.nav_companyjob).setVisible(false);
            menu.findItem(R.id.nav_training).setVisible(true);
            menu.findItem(R.id.nav_subscription).setVisible(true);
            menu.findItem(R.id.nav_package).setVisible(false);
            menu.findItem(R.id.nav_Applications).setVisible(false);
            menu.findItem(R.id.nav_recomend).setVisible(true);
        } else {
            menu.findItem(R.id.nav_dashboard).setVisible(false);
            menu.findItem(R.id.nav_companydashboard).setVisible(true);
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_comapny_profile).setVisible(true);
            menu.findItem(R.id.nav_searchjob).setVisible(false);
            menu.findItem(R.id.nav_companyjob).setVisible(true);
            menu.findItem(R.id.nav_training).setVisible(false);
            menu.findItem(R.id.nav_subscription).setVisible(false);
            menu.findItem(R.id.nav_package).setVisible(true);
            menu.findItem(R.id.nav_Applications).setVisible(true);
            menu.findItem(R.id.nav_recomend).setVisible(true);
        }

        menu.findItem(R.id.nav_logout).setVisible(true);
    }

    private void handleMenuClicks() {

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_dashboard)
                startActivity(new Intent(this, UserDashboard.class));

            else if (id == R.id.nav_companydashboard)
                startActivity(new Intent(this, CompanyDashboard.class));

            else if (id == R.id.nav_profile)
                startActivity(new Intent(this, Profile.class));

            else if (id == R.id.nav_comapny_profile)
                startActivity(new Intent(this, CompanyProfile.class));

            else if (id == R.id.nav_searchjob)
                startActivity(new Intent(this, Jobs.class));

            else if (id == R.id.nav_companyjob)
                startActivity(new Intent(this, Jobs.class));

            else if (id == R.id.nav_training)
                startActivity(new Intent(this, Courses.class));

            else if (id == R.id.nav_subscription)
                startActivity(new Intent(this, Subscription.class));

            else if (id == R.id.nav_package)
                startActivity(new Intent(this, Packages.class));

            else if (id == R.id.nav_Applications)
                startActivity(new Intent(this, Opportunity.class));

            else if (id == R.id.nav_recomend) {
                startActivity(new Intent(this, StoryListActivity.class));
            }

//            else if (id == R.id.nav_logout) {
//                sessionManager.logout();
//                Intent intent = new Intent(this, Login.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
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


            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
