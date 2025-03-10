package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class forgotPass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Button sendcode = (Button) findViewById(R.id.codesend);

        sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendingcode;
                sendingcode = new Intent(forgotPass.this, Verify.class);
                startActivity(sendingcode);

            }
        });

    }
}