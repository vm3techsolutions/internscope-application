package com.example.internscopeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;  // ✅ Use class variable
    private Button signin, viewjob, morejob, viewjob2; // ✅ Declare buttons as class variables
    private FirebaseAuth auth;
    private UserDatabseHelper dbHelper;
    private SessionManager sessionManager;
    private String currentUsername;


    private ImageView profileImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // ✅ Ensure correct layout file is loaded

        profileImg = findViewById(R.id.profile_img);

        // ✅ Ensure UI elements exist in XML before calling findViewById()
        drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            throw new NullPointerException("drawerLayout is null. Check activity_home.xml");
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        

        signin = findViewById(R.id.signinbtn);
        viewjob = findViewById(R.id.viewjob);
        viewjob2 = findViewById(R.id.viewjob2);

        // ✅ Prevent NullPointerException by checking if buttons exist before using them
        if (signin != null) {
            signin.setOnClickListener(v -> {
                Intent signinInt = new Intent(Home.this, Packages.class);
                startActivity(signinInt);
            });
        }

        if (viewjob != null) {
            viewjob.setOnClickListener(v -> {
                Intent intent = new Intent(Home.this, Jobs.class);
                startActivity(intent);
            });
        }


        if (viewjob2 != null) {
            viewjob2.setOnClickListener(v -> {
                Intent intent = new Intent(Home.this, Courses.class);
                startActivity(intent);
            });
        }

        // Initialize database helper and Firebase Auth
        auth = FirebaseAuth.getInstance();
        dbHelper = new UserDatabseHelper(this);
        sessionManager = new SessionManager(this);

        // Get logged-in user
        getCurrentUser();

        // ✅ Enable the "Hamburger" menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                 drawerLayout.addDrawerListener(toggle);
                 toggle.syncState();

        // Get header view from navigation drawer
        View headerView = navigationView.getHeaderView(0);
        profileImg = headerView.findViewById(R.id.nav_profile_img); // Profile image in drawer
        TextView usernameTextView = headerView.findViewById(R.id.nav_username);


        // Display username
        if (usernameTextView != null && currentUsername != null) {
            usernameTextView.setText(currentUsername);
        }

        // Load and display profile image
        loadProfileImage();



        // ✅ Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                startActivity(new Intent(Home.this, Profile.class));
            } else if (id == R.id.nav_searchjob) {
                startActivity(new Intent(Home.this, Jobs.class));
            } else if (id == R.id.nav_training) {
                startActivity(new Intent(Home.this, Courses.class));
            } else if (id == R.id.nav_subscription) {
                startActivity(new Intent(Home.this, Subscription.class));
            } else if (id == R.id.nav_package) {
                startActivity(new Intent(Home.this, Packages.class));
            } else if (id == R.id.nav_recomend) {
                startActivity(new Intent(Home.this, Opportunity.class));
            }

            // ✅ Close drawer after selection
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // Get the logged-in user
    private void getCurrentUser() {
        FirebaseUser googleUser = auth.getCurrentUser();
        String emailUser = sessionManager.getUsername();

        if (googleUser != null) {
            currentUsername = googleUser.getEmail();
        } else if (emailUser != null && !emailUser.isEmpty()) {
            currentUsername = emailUser;
        } else {
            currentUsername = "Guest";
        }
    }

    // Load and display profile image
    private void loadProfileImage() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = dbHelper.getProfileImage(currentUsername);
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profileImg.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "No profile image found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
