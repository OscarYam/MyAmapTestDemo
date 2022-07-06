package com.example.myamaptestdemo.ui.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<Integer> frameVisibility;


    public MutableLiveData<Integer> getFrameVisibility() {
        if (frameVisibility == null) {
            frameVisibility = new MutableLiveData<Integer>();
        }
        return frameVisibility;
    }

}