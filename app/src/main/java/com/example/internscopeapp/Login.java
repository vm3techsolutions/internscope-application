package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button userlogin;
    UserDatabseHelper dbHelper;
    SessionManager sessionManager;

    EditText userEmail, userPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        TextView forgot_pass =  (TextView) findViewById(R.id.forgot_pass);

        userlogin = (Button) findViewById(R.id.loginbtn);

        dbHelper = new UserDatabseHelper(this);
        sessionManager = new SessionManager(this);


        if (sessionManager.isLoggedIn()) {
            String savedUsername = sessionManager.getUsername();
            String savedPassword = dbHelper.getUserPassword(savedUsername);

            if (savedUsername != null && savedPassword != null) {
                userEmail.setBackgroundColor(getResources().getColor(R.color.yellow_shade));
                userPass.setBackgroundColor(getResources().getColor(R.color.yellow_shade));
                userEmail.setText(savedUsername);
                userPass.setText(savedPassword);
            }
        }else {
            startActivity(new Intent(Login.this, Home.class));
            finish();
        }

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userEmail.getText().toString().trim();
                String password = userPass.getText().toString().trim();

                if (dbHelper.checkUser(username, password)) {
                    sessionManager.saveLoginSession(username);
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, Home.class));
                    finish();
                } else {
                    Toast.makeText(Login.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forpass;
                forpass = new Intent(Login.this, forgotPass.class);
                startActivity(forpass);

            }
        });

        TextView signup = (TextView) findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent signUps;
                 signUps = new Intent(Login.this, SignUp.class);
                 startActivity(signUps);
            }
        });



    }
    private void loginUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = userEmail.getText().toString();
        String password = userPass.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Redirect to Home after successful login
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Login failed. Check credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}