package com.example.internscopeapp;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {

    private static final String HOST = "192.168.0.105";
    private static final String DATABASE = "user_management";
    private static final String PORT = "3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "ketaki2893";

    public Connection conn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Correct driver

            String connectionString = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
                    "?useSSL=false&allowPublicKeyRetrieval=true";

            conn = DriverManager.getConnection(connectionString, USERNAME, PASSWORD);
            Log.d("DB_SUCCESS", "Connection successful!");
        } catch (ClassNotFoundException e) {
            Log.e("DB_ERROR", "MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            Log.e("DB_ERROR", "SQL Exception: " + e.getMessage());
        }
        return conn;
    }
}
