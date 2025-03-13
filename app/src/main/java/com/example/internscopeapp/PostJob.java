package com.example.internscopeapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PostJob extends AppCompatActivity {

    private EditText jobTitle, vacancies, experienceLevel, jobTag, deadline, jobDescription;
    private Spinner jobCategory, jobType;
    private RadioGroup salaryOptions;
    private CheckBox termsConditions;
    private Button postJobButton;
    private ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_job);

        // Initialize views
        jobTitle = findViewById(R.id.job_title);
        vacancies = findViewById(R.id.vacancies);
        experienceLevel = findViewById(R.id.experience_level);
        jobTag = findViewById(R.id.job_tag);
        deadline = findViewById(R.id.deadline);
        jobDescription = findViewById(R.id.job_description);
        jobCategory = findViewById(R.id.job_category);
        jobType = findViewById(R.id.job_type);
        salaryOptions = findViewById(R.id.salary_options);
        termsConditions = findViewById(R.id.terms_conditions);
        postJobButton = findViewById(R.id.post_job);

        connectionClass = new ConnectionClass(); // Initialize DB connection

        // Set up Job Category options
        String[] categories = {"UI/UX Designer", "Front-End Developer", "Next.js", "Laravel Developer"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        jobCategory.setAdapter(categoryAdapter);

        // Set up Job Type options
        String[] jobTypes = {"Full Time", "Part Time"};
        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, jobTypes);
        jobType.setAdapter(jobTypeAdapter);

        // Handle button click
        postJobButton.setOnClickListener(v -> {
            if (termsConditions.isChecked()) {
                new SubmitJobTask().execute();
            } else {
                Toast.makeText(PostJob.this, "Please accept the terms & conditions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // AsyncTask to Insert Data into MySQL
    private class SubmitJobTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Connection conn = connectionClass.conn(); // Get database connection
                if (conn == null) {
                    return "Database connection failed!";
                }

                // Get selected salary option
                int selectedSalaryId = salaryOptions.getCheckedRadioButtonId();
                String salaryOption = selectedSalaryId != -1 ? ((RadioButton) findViewById(selectedSalaryId)).getText().toString() : "Not Specified";

                // SQL Query
                String query = "INSERT INTO post_job (job_title, job_category, vacancies, salary_option, job_type, experience_level, job_tag, deadline, job_description, terms_accepted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);

                // Bind Parameters
                stmt.setString(1, jobTitle.getText().toString());
                stmt.setString(2, jobCategory.getSelectedItem().toString());
                stmt.setInt(3, Integer.parseInt(vacancies.getText().toString()));
                stmt.setString(4, salaryOption);
                stmt.setString(5, jobType.getSelectedItem().toString());
                stmt.setString(6, experienceLevel.getText().toString());
                stmt.setString(7, jobTag.getText().toString());
                stmt.setString(8, deadline.getText().toString());
                stmt.setString(9, jobDescription.getText().toString());
                stmt.setBoolean(10, true); // Since terms are checked

                int rowsInserted = stmt.executeUpdate();
                stmt.close();
                conn.close();

                return rowsInserted > 0 ? "Job Posted Successfully!" : "Failed to post job!";
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error: " + e.getMessage());
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(PostJob.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
