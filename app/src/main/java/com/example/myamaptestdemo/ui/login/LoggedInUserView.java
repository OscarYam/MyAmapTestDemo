package com.example.myamaptestdemo.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String displayName;
    private String displayId;

    public LoggedInUserView(String displayName, String displayNameID) {
        this.displayName = displayName;
        this.displayId = displayNameID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayId() {
        return displayId;
    }
}