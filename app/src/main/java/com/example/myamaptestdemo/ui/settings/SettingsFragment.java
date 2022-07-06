package com.example.myamaptestdemo.ui.settings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.myamaptestdemo.MainActivityViewModel;
import com.example.myamaptestdemo.Utils;
import com.example.myamaptestdemo.databinding.SettingsFragmentBinding;
import com.example.myamaptestdemo.ui.login.LoginViewModel;
import com.example.myamaptestdemo.ui.login.LoginViewModelFactory;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private SettingsViewModel settingsViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private LoginViewModel loginViewModel;
    private UserInfoViewModel userInfoViewModel;
    private SettingsFragmentExViewModel settingsFragmentExViewModel;
    private SettingsFragmentBinding settingsFragmentBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        settingsFragmentBinding = SettingsFragmentBinding.inflate(inflater, container, false);
        return settingsFragmentBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity(), new LoginViewModelFactory()).get(LoginViewModel.class);
        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        settingsFragmentExViewModel = new ViewModelProvider(requireActivity()).get(SettingsFragmentExViewModel.class);
        // TODO: Use the ViewModel


        mainActivityViewModel.getGoHome().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    // 需要在返回主页前还原本页面
                    userinfoFragmentRecovery();
                }
            }
        });

        settingsFragmentBinding.clSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (Utils.back2PreviousFrame(motionEvent)) {
                    // 隐藏本层 frame, 并显示上一层 frame
//                    Log.e("settingsFragment", "返回上一层 frame");
                    settingsViewModel.getFrameVisibility().postValue(View.GONE);
                    settingsFragmentExViewModel.getClUsrInfoVisibility().postValue(View.VISIBLE);
                }
                return true;
            }
        });

        settingsFragmentBinding.svSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (Utils.back2PreviousFrame(motionEvent)) {
                    // 隐藏本层 frame, 并显示上一层 frame
//                    Log.e("settingsFragment", "返回上一层 frame");
                    settingsViewModel.getFrameVisibility().postValue(View.GONE);
                    settingsFragmentExViewModel.getClUsrInfoVisibility().postValue(View.VISIBLE);
                }
                return true;
            }
        });

        settingsFragmentBinding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.dbLogout();
            }
        });

        // 个人信息设置
        settingsFragmentBinding.llUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e("settingsFragment", "llUserInfo onClick");

                settingsViewModel.getFrameVisibility().postValue(View.GONE);
                userInfoViewModel.getFrameVisibility().postValue(View.VISIBLE);
            }
        });





    }

    /**
     * 还原页面
     * */
    private void userinfoFragmentRecovery() {
        // 还原 userinfoFragment 的页面
        // ......
    }

}