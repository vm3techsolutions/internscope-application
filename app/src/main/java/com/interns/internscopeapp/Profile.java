package com.interns.internscopeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//
//public class Profile extends AppCompatActivity {
//
//    private ImageView imageView;
//    private TextView tvFirstName, tvLastName, tvCurrentLocation, tvNationState,
//            tvCurrentJob, tvDesignation, tvQualification, tvLanguage,
//            tvDescription, tvEmail, tvPhone, tvLinkedIn, tvGender, tvResume;
//    private Button edit;
//
//    private SessionManager sessionManager;
//    //private RecyclerView recyclerView;
//    //private ApplicationAdapter adapter;
//    private List<JobApplication> applicationList = new ArrayList<>();
//
//    private ApiService apiService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        sessionManager = SessionManager.getInstance(this);
//        apiService = ApiClient.getClient(this).create(ApiService.class);
//
//        // Initialize profile views
//        imageView = findViewById(R.id.profile_img);
//        tvFirstName = findViewById(R.id.tvFirstName);
//        tvLastName = findViewById(R.id.tvLastName);
//        tvCurrentLocation = findViewById(R.id.tvCurrentLocation);
//        tvNationState = findViewById(R.id.tvNationState);
//        tvCurrentJob = findViewById(R.id.tvCurrentJob);
//        tvDesignation = findViewById(R.id.tvDesignation);
//        tvQualification = findViewById(R.id.tvQualification);
//        tvLanguage = findViewById(R.id.tvLanguage);
//        tvDescription = findViewById(R.id.tvDescription);
//        tvEmail = findViewById(R.id.tvEmail);
//        tvPhone = findViewById(R.id.tvPhone);
//        tvLinkedIn = findViewById(R.id.tvLinkedIn);
//        tvResume = findViewById(R.id.tvResume);
//        tvGender = findViewById(R.id.tvGender);
//        edit = findViewById(R.id.editprofile);
//
//        imageView.setImageResource(R.drawable.user_94);
//
//        // Setup RecyclerView
//       // recyclerView = findViewById(R.id.recyclerApplications);
//        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //adapter = new ApplicationAdapter(applicationList);
//        //recyclerView.setAdapter(adapter);
//
//        String token = sessionManager.getUserToken();
//        int userId = sessionManager.getUserId(); // ensure this is stored during login
//
//        if (token != null && !token.isEmpty() && userId > 0) {
//            fetchUserInfo(token);
//           // fetchAppliedJobs(token, userId);
//        } else {
//            Toast.makeText(this, "Please log in again.", Toast.LENGTH_SHORT).show();
//        }
//
//        edit.setOnClickListener(v -> startActivity(new Intent(Profile.this, EditProfiles.class)));
//    }
//
//    // ✅ Fetch user profile info
//    private void fetchUserInfo(String token) {
//        apiService.getUserProfile().enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    User user = response.body();
//
//                    tvFirstName.setText(safeText(user.getFirstName()));
//                    tvLastName.setText(safeText(user.getLastName()));
//                    tvCurrentLocation.setText( safeText(user.getCurrentLocation()));
//                    tvNationState.setText( safeText(user.getNationState()));
//                    tvCurrentJob.setText( safeText(user.getCurrentJobPlace()));
//                    tvDesignation.setText(safeText(user.getDesignation()));
//                    tvQualification.setText( safeText(user.getQualification()));
//                    tvLanguage.setText( safeText(user.getLanguage()));
//                    tvDescription.setText(safeText(user.getDescription()));
//                    tvEmail.setText( safeText(user.getEmail()));
//                    tvPhone.setText( safeText(user.getPhone()));
//                    tvLinkedIn.setText(safeText(user.getLinkedin()));
//                    //tvResume.setText(safeText(user.getResume()));
//                    tvGender.setText(safeText(user.getGender()));
//                    String resumeKey = safeText(user.getResume());
//
//                    if (!resumeKey.equals("N/A")) {
//                        tvResume.setText("📄 Resume: Uploaded");
//                    } else {
//                        tvResume.setText("📄 Resume: Not Uploaded");
//                    }
//                } else {
//                    Toast.makeText(Profile.this, "Failed to load profile info", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // ✅ Fetch applied jobs from API: /api/user/job/applied/{userId}
////    private void fetchAppliedJobs(String token, int userId) {
////        Call<JsonObject> call = apiService.getAppliedJobs("Bearer " + token, userId);
////
////        call.enqueue(new Callback<JsonObject>() {
////            @Override
////            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
////                if (response.isSuccessful() && response.body() != null) {
////                    JsonObject jsonResponse = response.body();
////                    JsonArray jobsArray = jsonResponse.getAsJsonArray("jobs");
////
////                    applicationList.clear();
////                    Gson gson = new Gson();
////
////                    for (JsonElement jobElement : jobsArray) {
////                        AppliedJob job = gson.fromJson(jobElement, AppliedJob.class);
////
////                        JobApplication app = new JobApplication();
////                        app.setJob_id(job.getJob_id());
////                        app.setJob_title(safeText(job.getJob_title()));
////                        app.setCompanyName(safeText(job.getCompany_name()));
////                        app.setLocation(safeText(job.getLocation()));
////                        app.setStatus(safeText(job.getStatus()));
////                        app.setApplied_at(safeText(job.getApply_date()));
////
////                        applicationList.add(app);
////                    }
////
////                    adapter.notifyDataSetChanged();
////                } else {
////                    Toast.makeText(Profile.this, "Failed to load applied jobs", Toast.LENGTH_SHORT).show();
////                    Log.e("Profile", "Response code: " + response.code());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<JsonObject> call, Throwable t) {
////                Toast.makeText(Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
////
//    // ✅ Safe text for null handling
//    private String safeText(String text) {
//        return text != null && !text.trim().isEmpty() ? text : "N/A";
//    }
//}

public class Profile extends BaseActivity {

    private ImageView imageView;
    private TextView tvFirstName, tvLastName, tvCurrentLocation, tvNationState,
            tvCurrentJob, tvDesignation, tvQualification, tvLanguage,
            tvDescription, tvEmail, tvPhone, tvLinkedIn, tvGender, tvResume, tvResumeUrl;
    private Button edit;

    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = SessionManager.getInstance(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        imageView = findViewById(R.id.profile_img);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvCurrentLocation = findViewById(R.id.tvCurrentLocation);
        tvNationState = findViewById(R.id.tvNationState);
        tvCurrentJob = findViewById(R.id.tvCurrentJob);
        tvDesignation = findViewById(R.id.tvDesignation);
        tvQualification = findViewById(R.id.tvQualification);
        tvLanguage = findViewById(R.id.tvLanguage);
        tvDescription = findViewById(R.id.tvDescription);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvLinkedIn = findViewById(R.id.tvLinkedIn);
        tvResume = findViewById(R.id.tvResume);
        tvResumeUrl = findViewById(R.id.tvResume);
        tvGender = findViewById(R.id.tvGender);
        edit = findViewById(R.id.editprofile);

        imageView.setImageResource(R.drawable.user_94);

        fetchUserInfo(sessionManager.getUserToken());

        edit.setOnClickListener(v -> startActivity(new Intent(Profile.this, EditProfiles.class)));

        setupDrawer();
    }

    private void fetchUserInfo(String token) {

        apiService.getUserProfile("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();

                    tvFirstName.setText(safeText(user.getFirstName()));
                    tvLastName.setText(safeText(user.getLastName()));
                    tvCurrentLocation.setText(safeText(user.getCurrentLocation()));
                    tvNationState.setText(safeText(user.getNationState()));
                    tvCurrentJob.setText(safeText(user.getCurrentJobPlace()));
                    tvDesignation.setText(safeText(user.getDesignation()));
                    tvQualification.setText(safeText(user.getQualification()));
                    tvLanguage.setText(safeText(user.getLanguage()));
                    tvDescription.setText(safeText(user.getDescription()));
                    tvEmail.setText(safeText(user.getEmail()));
                    tvPhone.setText(safeText(user.getPhone()));
                    tvLinkedIn.setText(safeText(user.getLinkedin()));
                    tvGender.setText(safeText(user.getGender()));

                    // Resume Display Logic
                    String resumeKey = user.getResume();

                    if (resumeKey != null && !resumeKey.trim().isEmpty()) {
                        tvResume.setText("📄 Resume: Uploaded");
                        fetchResumeUrl(resumeKey);
                    } else {
                        tvResume.setText("📄 Resume: Not Uploaded");
                        tvResumeUrl.setText("");
                    }

                } else {
                    Toast.makeText(Profile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchResumeUrl(String resumeKey) {

        String token = "Bearer " + sessionManager.getActiveToken();

        apiService.getResumeViewUrl(token, resumeKey).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String resumeUrl = response.body().getUrl();
                    tvResumeUrl.setText(resumeUrl);

                    tvResumeUrl.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resumeUrl));
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                tvResumeUrl.setText("Failed to load resume URL");
            }
        });
    }

    private String safeText(String text) {
        return text != null && !text.trim().isEmpty() ? text : "N/A";
    }
}
