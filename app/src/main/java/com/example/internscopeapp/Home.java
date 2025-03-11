package com.example.internscopeapp;

import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;  // ✅ Use class variable
    private Button signin, viewjob, morejob, viewjob2; // ✅ Declare buttons as class variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // ✅ Ensure correct layout file is loaded

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


        // ✅ Enable the "Hamburger" menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                 drawerLayout.addDrawerListener(toggle);
                 toggle.syncState();

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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
