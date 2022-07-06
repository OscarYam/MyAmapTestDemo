package com.example.myamaptestdemo.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
//    @Nullable
//    private Integer error;
    @Nullable
    private String error;

//    LoginResult(@Nullable Integer error) {
//        this.error = error;
//    }
    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    public LoggedInUserView getSuccess() {
        return success;
    }

//    @Nullable
//    public Integer getError() {
//        return error;
//    }

    @Nullable
    public String getError() {
        return error;
    }
}