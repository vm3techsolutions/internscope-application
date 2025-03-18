package com.example.internscopeapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "LoginSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private Context context;

    public SessionManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null"); // Prevent crashes
        }
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void saveLoginSession(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }


    public String getUsername() {
        return sharedPreferences.getString("username", ""); // Return empty string if null
    }


    public void logout() {
        editor.clear();
        editor.apply();
    }

}
