package com.example.myamaptestdemo.ui.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myamaptestdemo.ui.login.LoggedInUserView;

public class SettingsFragmentExViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<Integer> frameVisibility;
    private MutableLiveData<Integer> clUsrInfoVisibility;
    private MutableLiveData<LoggedInUserView> loggedInUser;
    private MutableLiveData<String> userImagePath;
    private MutableLiveData<String> userName;

    public MutableLiveData<Integer> getFrameVisibility() {
        if (frameVisibility == null) {
            frameVisibility = new MutableLiveData<Integer>();
        }
        return frameVisibility;
    }

    public MutableLiveData<Integer> getClUsrInfoVisibility() {
        if (clUsrInfoVisibility == null) {
            clUsrInfoVisibility = new MutableLiveData<Integer>();
        }
        return clUsrInfoVisibility;
    }

    public MutableLiveData<LoggedInUserView> getLoggedInUser() {
        if (loggedInUser == null) {
            loggedInUser = new MutableLiveData<LoggedInUserView>();
        }
        return loggedInUser;
    }

    public MutableLiveData<String> getUserImagePath() {
        if (userImagePath == null) {
            userImagePath = new MutableLiveData<String>();
        }
        return userImagePath;
    }

    public MutableLiveData<String> getUserName() {
        if (userName == null) {
            userName = new MutableLiveData<String>();
        }
        return userName;
    }

}