//package com.interns.internscopeapp;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import java.util.HashMap;
//
//public class SessionManager {
//
//    private static final String PREF_NAME = "LoginSession";
//    private static final String KEY_USER_ID = "user_id";
//    private static final String KEY_USERNAME = "username";
//    private static final String KEY_FULL_NAME = "full_name";
//
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_TOKEN = "token";
//    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
//    private static final String KEY_USER_TYPE = "user_type"; // 👈 NEW (student / company)
//
//
//
//
//    private static SessionManager instance;
//    private final SharedPreferences sharedPreferences;
//    private final SharedPreferences.Editor editor;
//
//    // Private constructor (singleton)
//    public SessionManager(Context context) {
//        sharedPreferences = context.getApplicationContext()
//                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//    }
//
//    // Singleton instance
//    public static synchronized SessionManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new SessionManager(context);
//        }
//        return instance;
//    }
//
//    // Save login session
//    public void saveLoginSession(int userId,String fullName, String username, String email, String token, String userType) {
//        editor.putInt(KEY_USER_ID, userId);
//        editor.putString(KEY_FULL_NAME, fullName);
//        editor.putString(KEY_USERNAME, username);
//        editor.putString(KEY_EMAIL, email);
//        editor.putString(KEY_TOKEN, token);
//        editor.putString(KEY_USER_TYPE, userType); // 👈 Save user type
//        editor.putBoolean(KEY_IS_LOGGED_IN, true);
//        editor.apply();
//    }
//
//    public void saveToken(String token) {
//        editor.putString(KEY_TOKEN, token);
//        editor.apply();
//    }
//
//
//    // Check if user is logged in
//    public boolean isLoggedIn() {
//        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
//    }
//
//    // Getters for user info
//    public int getUserId() {
//        return sharedPreferences.getInt(KEY_USER_ID, -1);
//    }
//
//    public String getUsername() {
//        return sharedPreferences.getString(KEY_USERNAME, "");
//    }
//
//    public String getUserFullName() {return sharedPreferences.getString(KEY_FULL_NAME, "");    }
//
//
//    public String getEmail() {
//        return sharedPreferences.getString(KEY_EMAIL, "");
//    }
//
//    public String getToken() {
//        return sharedPreferences.getString(KEY_TOKEN, "");
//    }
//
//    public String getUserType() {
//        return sharedPreferences.getString(KEY_USER_TYPE, ""); // 👈 default type
//    }
//
//    public String getUserRole() { return getUserType(); }
//    // Logout user
//    public void logout() {
//        editor.clear();
//        editor.apply();
//    }
//
//}

package com.interns.internscopeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private static final String PREF_NAME = "LoginSession";

    // Common user/company fields
    private static final String KEY_TOKEN = " ";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";

    // Tokens
    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_COMPANY_TOKEN = "company_token";

    // User type flag (student / company)
    private static final String KEY_USER_TYPE = "user_type";

    // Login flag
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // Company-specific fields (optional for future use)
    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_COMPANY_ID = "company_id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_RESUME_URL = "resume_url";

    private static SessionManager instance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    // ====== Constructor ======
    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    // ==========================================================
    // ✅ SAVE METHODS
    // ==========================================================

    // Save login session for user or company
    public void saveLoginSession(int id, String fullName, String username, String email,
                                 String token, String userType) {

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USER_TYPE, userType);

        if ("company".equalsIgnoreCase(userType)) {
            editor.putString(KEY_COMPANY_TOKEN, token);
            editor.putString(KEY_COMPANY_NAME, fullName);
            editor.putInt(KEY_COMPANY_ID, id);
        } else {
            editor.putString(KEY_USER_TOKEN, token);
        }

        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();

        Log.d("SessionManager", "Session saved: type=" + userType + ", id=" + id);
    }

    public void saveField(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void saveResumeUrl(String url) {
        editor.putString(KEY_RESUME_URL, url);
        editor.apply();
    }
    // ==========================================================
    // ✅ GETTERS (ALL FIELDS)
    // ==========================================================

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getToken() { return prefs.getString(KEY_TOKEN, "");
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public int getCompanyId() {
        return prefs.getInt(KEY_COMPANY_ID, -1);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE, "");
    }

    public String getPhone() {
        return prefs.getString(KEY_PHONE, "");
    }

    public String getLocation() {
        return prefs.getString(KEY_LOCATION, "");
    }

    public String getResumeUrl() { return prefs.getString(KEY_RESUME_URL, "");}

    public String getCompanyName() {
        return prefs.getString(KEY_COMPANY_NAME, "");
    }

    public String getUserToken() {
        return prefs.getString(KEY_USER_TOKEN, "");
    }

    public String getCompanyToken() {
        return prefs.getString(KEY_COMPANY_TOKEN, "");
    }

    // ✅ Automatically decide which token to use
    public String getActiveToken() {
        if ("company".equalsIgnoreCase(getUserType())) {
            return getCompanyToken();
        } else {
            return getUserToken();
        }
    }

    // ==========================================================
    // ✅ UPDATE / REMOVE METHODS
    // ==========================================================

    public void updateToken(String newToken) {
        if ("company".equalsIgnoreCase(getUserType())) {
            editor.putString(KEY_COMPANY_TOKEN, newToken);
        } else {
            editor.putString(KEY_USER_TOKEN, newToken);
        }
        editor.apply();
    }

    // ✅ Force use of the same token (JWT_TOKEN) for both user & company logins
//    public String getActiveToken() {
//        String userType = getUserType();
//        String token = getToken(); // default
//        // Force both user and company to use the same token key
//        if (token == null || token.isEmpty()) {
//
//            token = prefs.getString(KEY_TOKEN, "");
//        }
//        return token;
//    }

    public void clearCompanyData() {
        editor.remove(KEY_COMPANY_TOKEN);
        editor.remove(KEY_COMPANY_NAME);
        editor.remove(KEY_COMPANY_ID);
        editor.apply();
    }

    public void logout() {
        editor.clear();
        editor.apply();
        Log.d("SessionManager", "Session cleared");
    }

    // ==========================================================
    // ✅ DEBUG / LOGGING
    // ==========================================================

    public void logSessionInfo() {
        Log.d("SessionManager", "================ SESSION INFO ================");
        Log.d("SessionManager", "User ID: " + getUserId());
        Log.d("SessionManager", "User Type: " + getUserType());
        Log.d("SessionManager", "Full Name: " + getFullName());
        Log.d("SessionManager", "Email: " + getEmail());
        Log.d("SessionManager", "Company Name: " + getCompanyName());
        Log.d("SessionManager", "Active Token: " + (getActiveToken() != null ? getActiveToken().substring(0, Math.min(15, getActiveToken().length())) + "..." : "NULL"));
        Log.d("SessionManager", "=============================================");
    }
}
//package com.interns.internscopeapp;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//public class SessionManager {
//
//    private static final String PREF_NAME = "LoginSession";
//
//    // Shared keys
//    private static final String KEY_USER_ID = "user_id";
//    private static final String KEY_USERNAME = "username";
//    private static final String KEY_FULL_NAME = "full_name";
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_USER_TYPE = "user_type"; // "user" or "company"
//    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
//
//    // Tokens
//    private static final String KEY_USER_TOKEN = "user_token";
//    private static final String KEY_COMPANY_TOKEN = "company_token";
//
//    private static SessionManager instance;
//    private final SharedPreferences prefs;
//    private final SharedPreferences.Editor editor;
//
//    // Constructor (Singleton)
//    private SessionManager(Context context) {
//        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = prefs.edit();
//    }
//
//    public static synchronized SessionManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new SessionManager(context.getApplicationContext());
//        }
//        return instance;
//    }
//
//    // ======================================================
//    // ✅ SAVE METHODS
//    // ======================================================
//    public void saveLoginSession(int id, String fullName, String username, String email, String token, String userType) {
//        editor.putInt(KEY_USER_ID, id);
//        editor.putString(KEY_FULL_NAME, fullName);
//        editor.putString(KEY_USERNAME, username);
//        editor.putString(KEY_EMAIL, email);
//        editor.putString(KEY_USER_TYPE, userType.toLowerCase());
//        editor.putBoolean(KEY_IS_LOGGED_IN, true);
//
//        if ("company".equalsIgnoreCase(userType)) {
//            editor.putString(KEY_COMPANY_TOKEN, token);
//        } else {
//            editor.putString(KEY_USER_TOKEN, token);
//        }
//
//        editor.apply();
//
//        Log.d("SessionManager", "Saved session for type=" + userType +
//                " | token=" + token.substring(0, Math.min(15, token.length())) + "...");
//    }
//
//    // ======================================================
//    // ✅ GETTERS
//    // ======================================================
//
//    public boolean isLoggedIn() {
//        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
//    }
//
//    public int getUserId() {
//        return prefs.getInt(KEY_USER_ID, -1);
//    }
//
//    public String getUsername() {
//        return prefs.getString(KEY_USERNAME, "");
//    }
//
//    public String getFullName() {
//        return prefs.getString(KEY_FULL_NAME, "");
//    }
//
//    public String getEmail() {
//        return prefs.getString(KEY_EMAIL, "");
//    }
//
//    public String getUserType() {
//        return prefs.getString(KEY_USER_TYPE, "");
//    }
//
//    public String getUserToken() {
//        return prefs.getString(KEY_USER_TOKEN, "");
//    }
//
//    public String getCompanyToken() {
//        return prefs.getString(KEY_COMPANY_TOKEN, "");
//    }
//
//    // ✅ Get active token automatically based on user type
//    public String getActiveToken() {
//        String type = getUserType();
//        if ("company".equalsIgnoreCase(type)) {
//            return getCompanyToken();
//        } else {
//            return getUserToken();
//        }
//    }
//
//    // ======================================================
//    // ✅ UPDATE METHODS
//    // ======================================================
//    public void updateToken(String newToken) {
//        if ("company".equalsIgnoreCase(getUserType())) {
//            editor.putString(KEY_COMPANY_TOKEN, newToken);
//        } else {
//            editor.putString(KEY_USER_TOKEN, newToken);
//        }
//        editor.apply();
//        Log.d("SessionManager", "Token updated successfully for " + getUserType());
//    }
//
//    // ======================================================
//    // ✅ CLEAR / LOGOUT
//    // ======================================================
//    public void logout() {
//        editor.clear();
//        editor.apply();
//        Log.d("SessionManager", "User logged out. Session cleared.");
//    }
//
//    // ======================================================
//    // ✅ DEBUG LOGS
//    // ======================================================
//    public void logSession() {
//        Log.d("SessionManager", "============ SESSION INFO ============");
//        Log.d("SessionManager", "User ID: " + getUserId());
//        Log.d("SessionManager", "Full Name: " + getFullName());
//        Log.d("SessionManager", "Username: " + getUsername());
//        Log.d("SessionManager", "Email: " + getEmail());
//        Log.d("SessionManager", "User Type: " + getUserType());
//        Log.d("SessionManager", "Active Token: " +
//                (getActiveToken() != null && !getActiveToken().isEmpty()
//                        ? getActiveToken().substring(0, Math.min(20, getActiveToken().length())) + "..."
//                        : "NULL"));
//        Log.d("SessionManager", "=====================================");
//    }
//}
