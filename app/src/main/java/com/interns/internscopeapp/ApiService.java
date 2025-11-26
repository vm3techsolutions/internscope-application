package com.interns.internscopeapp;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ================= TEST =================
//    @GET("api")
//    Call<Map<String, String>> testConnection();


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
//    @GET("api/user/myprofile")
//    Call<User> getUserProfile(@Header("Authorization") String token);
//    @GET("api/user/myprofile")
//    Call<User> getUserProfile();

    @GET("api/user/myprofile")
    Call<User> getUserProfile(
            @Header("Authorization") String token
    );

    @POST("api/user/myprofile")
    Call<UserResponse> updateProfile(@Header("Authorization") String token, @Body UserRequest userRequest);

    //===================Company======================
    @GET("api/company/myprofile/{company_id}")
    Call<CompanyProfileModel> getCompanyProfile(@Path("company_id") int company_id);

    @POST("api/company/getSignedUrl")
    Call<SignedUrlResponse> getSignedUrl(@Body FileRequestBody body);


    @POST("api/company/myprofile")
    Call<Void> updateCompanyProfile(@Body RequestBody body);

//    @GET("api/company/myprofile")
//    Call<CompanyProfileResponse> getCompanyProfile(
//            @Header("Authorization") String token);

    @GET("api/company/myprofile")
    Call<CompanyProfileResponse> getCompanyProfile();

    @GET("api/company/view-certificate")
    Call<UploadResponse> getCompanyDocViewUrl(
            @Header("Authorization") String token,
            @Query("key") String s3Key
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

    @POST("api/user/getSignedUrl")
    Call<SignedUrlResponse> getSignedUrl(
            @Header("Authorization") String authHeader,
            @Body ResumeFileRequest request
    );

    @GET("api/user/view-resume")
    Call<UploadResponse> getResumeViewUrl(
            @Header("Authorization") String authHeader,
            @Query("key") String key
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

    @POST("api/user/apply/{job_id}")
    Call<GenericResponse> applyJob(
            @Header("Authorization") String token,
            @Path("job_id") int jobId,
            @Body ApplyJobRequest applyRequest
    );
//    @POST("api/user/apply/{job_id}")
//    Call<GenericResponse> applyJob(
//            @Header("Authorization") String token,
//            @Body ApplyJobRequest body
//    );

    @GET("api/user/job/applied")
    Call<List<AppliedJobResponse>> getAppliedJobs(@Header("Authorization") String token);
    //@GET("api/user/job/applied")
    //Call<List<AppliedJobResponse>> getAppliedJobs();

    @POST("api/user/job/apply/{job_id}")
    Call<Void> applyJob(@Path("job_id") int jobId, @Body ApplyJobRequestBody body);

    @GET("api/user/job/applied/{user_id}")
    Call<JsonObject> getAppliedJobs(
            @Header("Authorization") String token,
            @Path("user_id") int userId
    );


//    @GET("api/user/job/applied/{userId}")
//    Call<JsonObject> getAppliedJobs(@Header("Authorization") String token, @Path("userId") int userId);

    //===================Company Job List==================

    @GET("api/company/jobs/getAllByCompany/{company_id}")
    Call<List<CompanyJobModel>> getJobsByCompany(
            @Header("Authorization") String token,
            @Path("company_id") int companyId
    );


    @POST("api/company/jobs")
    Call<UpdateResponse> createJobPost(
            @Header("Authorization") String token,
            @Body JobPostRequest jobPost
    );
    @GET("api/company/jobs/{id}")
    Call<CompanyJobModel> getJobById(@Header("Authorization") String token, @Path("id") int jobId);

    @PUT("api/company/jobs/{id}")
    Call<UpdateResponse> updateJobPost(
            @Header("Authorization") String authToken,
            @Path("id") int jobId,
            @Body CompanyJobModel jobPost
    );




    //================fetch pricing plan==================

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

    // ================= CART =================

    // ✅ Fetch cart items (token-based, same route style as backend)
    @GET("api/cart")
    Call<CartResponse> getUserCart(@Header("Authorization") String bearerToken);

    @GET("api/company/cart")
    Call<CartResponse> getCompanyCart(@Header("Authorization") String bearerToken);


    // ✅ Update cart quantity
    @PUT("api/cart/{id}")
    Call<GenericResponse> updateUserCartQuantity(
            @Header("Authorization") String bearerToken,
            @Path("id") int cartItemId,
            @Body UpdateQtyRequest body
    );

    @PUT("api/company/cart/{id}")
    Call<GenericResponse> updateCompanyCartQuantity(
            @Header("Authorization") String bearerToken,
            @Path("id") int cartItemId,
            @Body UpdateQtyRequest body
    );

    // ✅ Remove items from cart
    @DELETE("api/cart/{id}")
    Call<GenericResponse> removeUserCartItem(
            @Header("Authorization") String bearerToken,
            @Path("id") int cartItemId
    );

    @DELETE("api/company/cart/{id}")
    Call<GenericResponse> removeCompanyCartItem(
            @Header("Authorization") String bearerToken,
            @Path("id") int cartItemId
    );

//    @POST("api/cart")
//    Call<ApiResponse> addToCart(@Header("Authorization") String token, @Body AddCartRequest cartRequest);

    @POST("api/cart")
    Call<ApiResponse> addToCart(@Header("Authorization") String bearerToken,@Body AddCartRequest request);

    @POST("api/company/cart")
    Call<ApiResponse> addCompanyCart(@Header("Authorization") String token, @Body AddCartRequest cartRequest);

//
//    // ================= PAYMENTS =================


    @POST("api/payment/initiate")
    Call<ApiRequestResponse> initiatePayment(
            @Header("Authorization") String token,
            @Body PhonePePaymentRequest request
    );

    @POST("api/company/payment/initiate")
    Call<ApiRequestResponse> initiateCompanyPayment(
            @Header("Authorization") String token,
            @Body PhonePePaymentRequest request
    );

    // Verification (optional — backend must implement)
    @GET("api/payment/verify/{transactionId}")
    Call<PaymentStatusResponse> verifyUserPayment(
            @Header("Authorization") String token,
            @Path("transactionId") String transactionId
    );

    // For company payments
    @GET("api/company/payment/verify/{transactionId}")
    Call<PaymentStatusResponse> verifyCompanyPayment(
            @Header("Authorization") String token,
            @Path("transactionId") String transactionId
    );

    //=========================Company Dashboard=========

    @GET("api/dashboard/stats/{companyId}")
    Call<DashboardStatsResponse> getDashboardStats(@Path("companyId") int companyId);

    @GET("api/dashboard/applied-candidates/{companyId}")
    Call<List<CandidateResponse>> getAppliedCandidates(@Path("companyId") int companyId);

    @PUT("api/user/job/applied/{job_id}")
    Call<Void> updateCandidateStatus(
            @Header("Authorization") String token,
            @Path("job_id") int jobId,
            @Body UpdateStatusRequest body
    );

    @PUT("api/company/job/application/{application_id}")
    Call<Void> updateCompanyCandidateStatus(
            @Header("Authorization") String token,
            @Path("application_id") int applicationId,
            @Body UpdateStatusRequest body
    );

    // company stats - requires Authorization header
//    @GET("api/company/transaction/details")
//    Call<DashboardStatsResponse> getDashboardStats(@Header("Authorization") String token);
//
//    // applied candidates - requires Authorization header
//    @GET("api/company/job/recived")
//    Call<List<CandidateResponse>> getAppliedCandidates(@Header("Authorization") String token);

    @GET("api/company/transaction/details")
    Call<DashboardStatsResponse> getDashboardStats();

    @GET("api/company/job/recived")
    Call<List<CandidateResponse>> getAppliedCandidates();

    //===============User Dashboard=========

    @GET("api/user/job/applied/{user_id}")
    Call<JsonObject> getAppliedJobsByUser(
            @Path("user_id") int userId,
            @Header("Authorization") String token
    );





    //===================Forgot Password ================

    // User routes
    @FormUrlEncoded
    @POST("api/forgot-password")
    Call<ApiResponse> forgotUserPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("api/reset-password")
    Call<ApiResponse> resetUserPassword(@Field("token") String token, @Field("newPassword") String newPassword);

    // Company routes
    @FormUrlEncoded
    @POST("api/company/forgotCompany-password")
    Call<ApiResponse> forgotCompanyPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("api/company/resetCompany-password")
    Call<ApiResponse> resetCompanyPassword(@Field("token") String token, @Field("newPassword") String newPassword);

}


