package com.example.internscopeapp;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // ================= TEST =================
    @GET("api")
    Call<Map<String, String>> testConnection();


    // ================= AUTH =================
    @POST("user/register")
    Call<UserResponse> registerUser(@Body SignupRequest signupRequest);

    @POST("user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    //=============Company Auth============
    // Company Login
    @POST("company/login")
    Call<LoginResponse> loginCompany(@Body LoginRequest request);

//    @POST("company/register")
//    Call<CompanyRegisterResponse> registerCompany(@Body JsonObject body);

    @POST("company/register")  // Update to your correct backend route
    Call<UserResponse> registerCompany(@Body CompanySignupRequest request);

    // ================= PROFILE =================
    //==================Candidate=================
    @GET("api/user/myprofile")
    Call<User> getUserProfile(@Header("Authorization") String token);

    @POST("api/user/myprofile")
    Call<UserResponse> updateProfile(@Header("Authorization") String token, @Body UserRequest userRequest);

    //===================Company======================
    @GET("api/company/myprofile/{company_id}")
    Call<CompanyProfileModel> getCompanyProfile(@Path("company_id") int company_id);

    @Multipart
    @POST("api/company/myprofile")
    Call<Void> updateCompanyProfile(
            @Header("Authorization") String token,
            @Part("company_name") RequestBody companyName,
            @Part("company_type") RequestBody companyType,
            @Part("company_size") RequestBody companySize,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("location") RequestBody location,
            @Part("state") RequestBody state,
            @Part("website") RequestBody website,
            @Part("linkedin") RequestBody linkedin,
            @Part("kyc_method") RequestBody kycMethod,
            @Part("certificate_number") RequestBody certificateNumber,
            @Part("about") RequestBody about,
            @Part MultipartBody.Part certificateFile
    );



    @POST("api/company/getSignedUrl")
    Call<SignedUrlResponse> getSignedUrl(
            @Header("Authorization") String token,
            @Body Map<String, String> body
    );

    @POST("api/company/myprofile")
    Call<Void> postCompanyProfile(
            @Header("Authorization") String token,
            @Body Map<String, String> body
    );


    // ================= JOBS =================
    @GET("api/jobs/AllJob")
    Call<List<JobResponse>> getAllJobs();

    @GET("api/jobs/AllJobPost")
    Call<List<JobResponse>> getAllJobPost();

    // ✅ Get job details including company_id
    @GET("api/company/jobs/{id}")
    Call<JobResponse> getCompanyId(@Path("id") int jobId);


    // ✅ Always wrap in list, even if backend sends single object
    @GET("api/jobs/{job_id}")
    Call<JobResponse> getJobDetails(@Path("job_id") int jobId);

    // Upload resume
    // ================= RESUME UPLOAD =================
    @Multipart
    @POST("api/upload/resume")
    Call<UploadResponse> uploadResume(
            @Header("Authorization") String token,
            @Part MultipartBody.Part resume
    );

    // ================= APPLY JOB =================
    // Apply job using resume URL (no file part needed)
    @Multipart
    @POST("api/user/job/apply/{job_id}")
    Call<ApplyJobResponse> applyJobWithFile(
            @Header("Authorization") String token,
            @Path("job_id") int jobId,
            @Part("fullName") RequestBody fullName,
            @Part("email") RequestBody email,
            @Part("company_id") RequestBody companyId,
            @Part("resume_url") RequestBody resumeUrl
    );

    // Apply job using JSON instead of multipart
    @POST("api/user/job/apply/{job_id}")
    Call<ApplyJobResponse> applyJobWithJson(
            @Header("Authorization") String token,
            @Path("job_id") int jobId,
            @Body JsonObject body
    );

    //@GET("api/user/applied")
    //Call<List<AppliedJobResponse>> getAppliedJobs();
   // Call<List<AppliedJobResponse>> getAppliedJobs(@Header("Authorization") String token);

    @GET("api/user/job/applied")
    Call<List<AppliedJobResponse>> getAppliedJobs(@Header("Authorization") String token);
    //@GET("api/user/job/applied")
    //Call<List<AppliedJobResponse>> getAppliedJobs();

    @POST("api/user/job/apply/{job_id}")
    Call<Void> applyJob(@Path("job_id") int jobId, @Body ApplyJobRequestBody body);

    @GET("api/user/job/applied/{user_id}")
    Call<AppliedJobsResponse> getAppliedJobsByUser(
            @Header("Authorization") String token,
            @Path("user_id") int userId
    );

    @GET("api/user/job/applied/{userId}")
    Call<JsonObject> getAppliedJobs(@Header("Authorization") String token, @Path("userId") int userId);

    //fetch pricing plan

    @GET("api/plans")
    Call<List<Plan>> getPlans();

    @GET("api/plans/{user_type}")
    Call<List<Plan>> getPlansByUserType(@Path("user_type") String userType);

    //Payment

    @POST("api/payment/initiate")
    Call<JsonObject> initiatePhonePePayment(@Body JsonObject body);

    @POST("api/payment/phonepe-callback")
    Call<JsonObject> sendPhonePeCallback(@Body JsonObject body);

    @POST("api/payment/phonepe-callback")
    Call<JsonObject> verifyPhonePePayment(@Body JsonObject body);

    @GET("api/user/transaction/history")
    Call<JsonObject> getUserTransactions(@Header("Authorization") String token);












    // ================= SKILLS =================
//    @GET("user/getSkills")
//    Call<List<SkillResponse>> getSkills(@Header("Authorization") String token);
//
//    @POST("user/updateSkills")
//    Call<ApiResponse> addSkill(
//            @Header("Authorization") String token,
//            @Body SkillRequest skillRequest
//    );
//
//    @DELETE("user/deleteSkill")
//    Call<ApiResponse> deleteSkill(@Header("Authorization") String token);
//
//
//    // ================= IT SKILLS =================
//    @GET("user/getITSkills")
//    Call<List<ITSkillResponse>> getITSkills(@Header("Authorization") String token);
//
//    @POST("user/updateITSkills")
//    Call<ApiResponse> addITSkill(
//            @Header("Authorization") String token,
//            @Body ITSkillRequest itSkillRequest
//    );
//
//    @DELETE("user/deleteITSkill")
//    Call<ApiResponse> deleteITSkill(@Header("Authorization") String token);
//
//
//    // ================= EDUCATION =================
//    @GET("user/education")
//    Call<List<EducationResponse>> getEducation(@Header("Authorization") String token);
//
//    @POST("user/education")
//    Call<ApiResponse> addEducation(
//            @Header("Authorization") String token,
//            @Body EducationRequest educationRequest
//    );
//
//    @DELETE("user/education/{educationId}")
//    Call<ApiResponse> deleteEducation(
//            @Header("Authorization") String token,
//            @Path("educationId") int educationId
//    );
//
//
//    // ================= PROJECTS =================
//    @GET("user/projects")
//    Call<List<ProjectResponse>> getProjects(@Header("Authorization") String token);
//
//    @POST("user/projects")
//    Call<ApiResponse> addProject(
//            @Header("Authorization") String token,
//            @Body ProjectRequest projectRequest
//    );
//
//    @DELETE("user/projects/{projectId}")
//    Call<ApiResponse> deleteProject(
//            @Header("Authorization") String token,
//            @Path("projectId") int projectId
//    );
//
//
//

//    @GET("user/job/applied")
//    Call<List<AppliedJobResponse>> getAppliedJobs(@Header("Authorization") String token);
//
//
//    // ================= CART =================
//    @POST("cart")
//    Call<ApiResponse> addToCart(
//            @Header("Authorization") String token,
//            @Body CartRequest cartRequest
//    );
//
//    @GET("cart")
//    Call<List<CartResponse>> getCart(@Header("Authorization") String token);
//
//    @DELETE("cart/{id}")
//    Call<ApiResponse> removeCartItem(
//            @Header("Authorization") String token,
//            @Path("id") int itemId
//    );
//
//
//    // ================= PAYMENTS =================
//    @POST("payment/initiate")
//    Call<PaymentResponse> initiatePayment(
//            @Header("Authorization") String token,
//            @Body PaymentRequest paymentRequest
//    );
//
//    @GET("user/transaction/history")
//    Call<List<TransactionResponse>> getTransactionHistory(@Header("Authorization") String token);
}
