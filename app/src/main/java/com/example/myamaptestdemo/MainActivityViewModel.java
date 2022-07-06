package com.example.myamaptestdemo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> goHome;
    private MutableLiveData<String> error;

    public MutableLiveData<Boolean> getGoHome() {
        if (goHome == null) {
            goHome = new MutableLiveData<Boolean>();
        }
        return goHome;
    }

    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<String>();
        }
        return error;
    }

}
