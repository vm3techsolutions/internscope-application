package com.interns.internscopeapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
     //private static final String BASE_URL = "http://192.168.0.104:4000/"; // ✅ Add trailing slash
    private static final String BASE_URL = "https://api.internscope.com";

    //private static final String BASE_URL = "https://planktonic-masterfully-joy.ngrok-free.dev/";
    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context)) // attach JWT automatically
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

//            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(new TypeToken<List<JobResponse>>(){}.getType(), new JobPostDeserializer())
//                    .create();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(List.class, new JobTagDeserializer()) // handles job_tag globally
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
