package com.example.internscopeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class UserDatabseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;  // Updated version to alter table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IMAGE = "profile_image";
    private static final String COLUMN_PROFILE_IMAGE = "profile_image"; // New column for image

    public UserDatabseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE users ("
                + "user_id TEXT PRIMARY KEY, "
                + "username TEXT, "
                + "email TEXT, "
                + "password TEXT, "
                + "profile_image BLOB)";
        db.execSQL(CREATE_USERS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users"); // Remove the old table
        onCreate(db); // Create a new table with updated schema
    }


    // Register user with default null profile image
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PROFILE_IMAGE, (byte[]) null); // Default null image
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Validate login
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Get user password
    public String getUserPassword(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_PASSWORD},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String password = cursor.getString(0);
            cursor.close();
            return password;
        }
        return null;
    }

    // Convert Bitmap to byte array
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Convert byte array to Bitmap
    private Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    // Save Profile Image
    public boolean saveProfileImage(String username, Bitmap bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (username == null || bitmap == null) {
            return false; // Prevent null values
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, imageBytes);

        // Check if user exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();

        long result;
        if (exists) {
            // Update image for existing user
            result = db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
        } else {
            // Insert new user with image (optional)
            values.put(COLUMN_USERNAME, username); // Required for inserting new user
            result = db.insert(TABLE_USERS, null, values);
        }

        db.close();
        return result != -1; // Return true if successful
    }



    // Get Profile Image
    public byte[] getProfileImage(String username) {
        if (username == null) {
            return null;  // Avoid passing null to SQLite query
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users",
                new String[]{"profile_image"},
                "username = ?",
                new String[]{username},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(0);
            cursor.close();
            return imageBytes;
        }
        return null;
    }

    public boolean insertGoogleUser(String userId, String userName, String userEmail, String userProfileImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);  // Ensure column name matches table schema
        values.put("username", userName);
        values.put("email", userEmail);
        values.put("profile_image", userProfileImage);

        long result = db.insert("users", null, values);
        db.close();
        return result != -1; // Returns true if insertion was successful
    }




}
