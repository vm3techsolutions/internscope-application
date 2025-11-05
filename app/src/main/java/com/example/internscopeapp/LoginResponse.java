package com.example.internscopeapp;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private User user;

    // ✅ Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public User getUser() { return user; }

    // ✅ Nested User class
    public static class User {
        private int id;
        private String username;
        private String email;

        // Getters
        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
    }
}
