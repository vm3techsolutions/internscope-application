package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Packages extends AppCompatActivity {

    private RadioGroup packageGroup;
    private Button postJobButton;
    private int selectedPackageLimit = 0;
    private  int jobPostedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_packages);

        packageGroup = findViewById(R.id.package_select);
        postJobButton = findViewById(R.id.postJobButton);

        packageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedPackageLimit = getJobLimit(checkedId);
                jobPostedCount = 0; // Reset job count when package changes
                Toast.makeText(Packages.this, "Package Selected: " + selectedPackageLimit + " job posts allowed", Toast.LENGTH_SHORT).show();
            }
        });

        postJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPackageLimit == 0) {
                    Toast.makeText(Packages.this, "Please select a package first!", Toast.LENGTH_SHORT).show();
                } else if (jobPostedCount < selectedPackageLimit) {
                    jobPostedCount++;
                    Intent postIntent = new Intent(Packages.this, PostJob.class);
                    startActivity(postIntent);
                } else {
                    Toast.makeText(Packages.this, "Job post limit reached! Upgrade your package.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private int getJobLimit(int checkedId) {
        if (checkedId == R.id.packof1) return 1;
        if (checkedId == R.id.packof2) return 2;
        if (checkedId == R.id.packof3) return 3;
        if (checkedId == R.id.packof4) return 4;
        if (checkedId == R.id.packof5) return 5;
        return 0;
    }
}