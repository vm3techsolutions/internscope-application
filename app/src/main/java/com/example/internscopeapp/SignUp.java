package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class SignUp extends AppCompatActivity {

    UserDatabseHelper dbHelper;
    EditText userEmail;
    EditText userPass;
    Button signUpbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        signUpbtn = findViewById(R.id.signUpbtn);
        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.userPass);
        dbHelper = new UserDatabseHelper(this);

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userEmail.getText().toString().trim();
                String userpaswrd = userPass.getText().toString().trim();

                if(username.isEmpty() || userpaswrd.isEmpty())
                {
                    Toast.makeText(SignUp.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                }else {
                    boolean success = dbHelper.registerUser(username, userpaswrd);
                    if (success) {
                        Toast.makeText(SignUp.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, Verify.class));
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}