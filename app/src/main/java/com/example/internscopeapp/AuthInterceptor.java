package com.example.internscopeapp;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Get token from SessionManager
        String token = SessionManager.getInstance(context).getActiveToken();
        android.util.Log.d("AuthInterceptor", "Token: " + token);

        if (token != null && !token.isEmpty()) {
            // Add Authorization header
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(originalRequest);
    }
}

//package com.example.internscopeapp;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.io.IOException;
//
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class AuthInterceptor implements Interceptor {
//
//    private final Context context;
//
//    public AuthInterceptor(Context context) {
//        this.context = context.getApplicationContext();
//    }
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request originalRequest = chain.request();
//
//        SessionManager session = SessionManager.getInstance(context);
//        String token = session.getActiveToken(); // ✅ automatically picks user/company
//
//        if (token != null && !token.isEmpty()) {
//            Log.d("AuthInterceptor", "Attaching Token: " + token.substring(0, Math.min(20, token.length())) + "...");
//            Request newRequest = originalRequest.newBuilder()
//                    .header("Authorization", "Bearer " + token)
//                    .build();
//            return chain.proceed(newRequest);
//        }
//
//        Log.w("AuthInterceptor", "No token found in session");
//        return chain.proceed(originalRequest);
//    }
//}
