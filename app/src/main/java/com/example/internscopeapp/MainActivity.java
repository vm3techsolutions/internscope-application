//package com.example.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.Task;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int RC_SIGN_IN = 9001;
//    private GoogleSignInClient googleSignInClient;
//
//    Button glogin, emailLogin;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//
//        glogin = findViewById(R.id.companyLogin);
//        emailLogin = findViewById(R.id.emailLogin);
//
//        // Configure Google Sign-In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        // Google Login Button
//        glogin.setOnClickListener(view -> signInWithGoogle());
//
//        // Email Login Button → open Login activity directly
//        emailLogin.setOnClickListener(view -> {
//            startActivity(new Intent(MainActivity.this, Login.class));
//        });
//    }
//
//    // Start Google Sign-In intent
//    private void signInWithGoogle() {
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    // Handle Google Sign-In result
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//
//                if (account != null) {
//                    String userName = account.getDisplayName();
//                    String userEmail = account.getEmail();
//                    String userProfileImage = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";
//
//                    Toast.makeText(this, "Google Sign-In Success: " + userEmail, Toast.LENGTH_LONG).show();
//
//                    // TODO: Send this data to your backend API if needed
//
//                    // Redirect to Home
//                    startActivity(new Intent(MainActivity.this, Home.class));
//                    finish();
//                }
//            } catch (ApiException e) {
//                Toast.makeText(this, "Google Sign-In Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//}

package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button emailLogin, companyLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        emailLogin = findViewById(R.id.emailLogin);
        companyLogin = findViewById(R.id.companyLogin);

        // Candidate Login
        emailLogin.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.putExtra("userType", "user");
            startActivity(intent);
        });

        // Company Login
        companyLogin.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Company_login.class);
            intent.putExtra("userType", "company");
            startActivity(intent);
        });
    }
}

