package com.example.myamaptestdemo.ui.settings;

import androidx.annotation.Nullable;

/**
 * Nickname validation state of the nickname form.
 */
 class NicknameFormState {
    @Nullable
    private String nicknameError;
    private boolean isDataValid;

    NicknameFormState(@Nullable String nicknameError) {
        this.nicknameError = nicknameError;
        this.isDataValid = false;
    }

    NicknameFormState(boolean isDataValid) {
        this.nicknameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    String getNicknameError() {
        return nicknameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}
