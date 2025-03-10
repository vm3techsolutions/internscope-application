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

public class Verify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify);
        Button verify = (Button) findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent verify;

                verify = new Intent(Verify.this, Home.class);

                startActivity(verify);

            }
        });
    }
}