package com.example.myamaptestdemo.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String userToken;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public LoggedInUser(String userId, String displayName, String userToken) {
        this.userId = userId;
        this.displayName = displayName;
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserToken() {
        return userToken;
    }
}