package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Handler;
import android.os.Looper;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ImageView topLogo = findViewById(R.id.topLogo);
        LinearLayout bottomLayout = findViewById(R.id.bottomLayout);

        // Animation 1 – Fade + Scale for Main Logo
        Animation fadeScale = AnimationUtils.loadAnimation(this, R.anim.fade_scale);
        topLogo.startAnimation(fadeScale);

        // Animation 2 – Slide Up for "Powered by" Section
        new Handler().postDelayed(() -> {
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            bottomLayout.startAnimation(slideUp);
            bottomLayout.setAlpha(1f);
        }, 600);   // delay bottom section animation

        // Move to Main Activity
//        new Handler().postDelayed(() -> {
//            startActivity(new Intent(SplashScreen.this, MainActivity.class));
//            finish();
//        }, 1600);

        // Move to correct screen after Splash
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            SessionManager sessionManager = SessionManager.getInstance(SplashScreen.this);

            if (sessionManager.isLoggedIn()) {

                String userType = sessionManager.getUserType();

                if ("user".equalsIgnoreCase(userType)) {
                    // Candidate user → user dashboard
                    startActivity(new Intent(SplashScreen.this, UserDashboard.class));
                } else {
                    // Company user → company dashboard
                    startActivity(new Intent(SplashScreen.this, CompanyDashboard.class));
                }

            } else {
                // Not logged in → Show correct login screen
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }

            finish();

        }, 1600);


    }
}