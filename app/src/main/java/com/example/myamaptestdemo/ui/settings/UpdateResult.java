package com.example.myamaptestdemo.ui.settings;

import androidx.annotation.Nullable;

import com.example.myamaptestdemo.data.User;
import com.example.myamaptestdemo.data.model.LoggedInUser;

public class UpdateResult {
    @Nullable
    private User success;
    @Nullable
    private String error;

    UpdateResult(@Nullable String error) {
        this.error = error;
    }

    UpdateResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
    public User getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }
}
