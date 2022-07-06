package com.example.myamaptestdemo.ui.settings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.example.myamaptestdemo.MainActivityViewModel;
import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.Utils;
import com.example.myamaptestdemo.data.AppDatabase;
import com.example.myamaptestdemo.data.User;
import com.example.myamaptestdemo.data.model.LoggedInUser;
import com.example.myamaptestdemo.databinding.SettingsFragmentExFragmentBinding;
import com.example.myamaptestdemo.ui.login.LoginViewModel;
import com.example.myamaptestdemo.ui.login.LoginViewModelFactory;
import java.io.File;


public class SettingsFragmentEx extends Fragment {

    public static SettingsFragmentEx newInstance() {
        return new SettingsFragmentEx();
    }

    private SettingsFragmentExViewModel settingsFragmentExViewModel;
    private LoginViewModel loginViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private UserInfoViewModel userInfoViewModel;
    private SettingsViewModel settingsViewModel;

    private SettingsFragmentExFragmentBinding settingsFragmentExFragmentBinding;

    private FrameLayout fcvUsrInfo;
    private FrameLayout fcvSettings;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        settingsFragmentExFragmentBinding = SettingsFragmentExFragmentBinding.inflate(inflater, container, false);

        return settingsFragmentExFragmentBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingsFragmentExViewModel = new ViewModelProvider(requireActivity()).get(SettingsFragmentExViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity(), new LoginViewModelFactory()).get(LoginViewModel.class);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);


        fcvUsrInfo = settingsFragmentExFragmentBinding.fcvUsrInfo;
        fcvUsrInfo.setVisibility(View.GONE);
        fcvSettings = settingsFragmentExFragmentBinding.fcvSettings;
        fcvSettings.setVisibility(View.GONE);

        // TODO: Use the ViewModel
        mainActivityViewModel.getGoHome().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // 需要在返回主页前还原当前的页面
                settingsFragmentExRecovery();
            }
        });

        settingsFragmentExViewModel.getClUsrInfoVisibility().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == View.GONE) {
                    Utils.addAnimation(settingsFragmentExFragmentBinding.clUsrInfo, View.GONE, 1);
                }

                if (integer == View.VISIBLE) {
                    Utils.addAnimation(settingsFragmentExFragmentBinding.clUsrInfo, View.VISIBLE, 1);
                }
            }
        });

        userInfoViewModel.getFrameVisibility().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == View.GONE) {
                    Utils.addAnimation(fcvUsrInfo, View.GONE, 1);

                }

                if (integer == View.VISIBLE) {
                    Utils.addAnimation(fcvUsrInfo, View.VISIBLE, 1);
                }
            }
        });

        settingsViewModel.getFrameVisibility().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == View.GONE) {
                    Utils.addAnimation(fcvSettings, View.GONE, 1);
                }

                if (integer == View.VISIBLE) {
                    Utils.addAnimation(fcvSettings, View.VISIBLE, 1);
                }
            }
        });

        settingsFragmentExFragmentBinding.llSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

//                Log.e("settingsFragment","frame onTouch");

                if (Utils.back2PreviousFrame(motionEvent)) {
                    // 隐藏本层 frame, 并显示上一层 frame
//                    Log.e("settingsFragment", "返回上一层 frame");
                    settingsFragmentExViewModel.getFrameVisibility().postValue(View.GONE);
                }
                return true;

            }
        });


        settingsFragmentExFragmentBinding.imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fcvSettings.getVisibility() != View.VISIBLE) {
                    Utils.addAnimation(fcvSettings, View.VISIBLE, 1);
                    Utils.addAnimation(settingsFragmentExFragmentBinding.clUsrInfo, View.GONE, 1);
                    settingsFragmentExFragmentBinding.clUsrInfo.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            settingsFragmentExFragmentBinding.clUsrInfo.setVisibility(View.GONE);
                            settingsFragmentExFragmentBinding.scrollviewUsrInfo.scrollTo(0, 0); // scrollView 回到顶部位置
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        settingsFragmentExFragmentBinding.imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fcvUsrInfo.getVisibility() != View.VISIBLE) {
                    Utils.addAnimation(fcvUsrInfo, View.VISIBLE, 1);;
                    Utils.addAnimation(settingsFragmentExFragmentBinding.clUsrInfo, View.GONE, 1);
                    settingsFragmentExFragmentBinding.clUsrInfo.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            settingsFragmentExFragmentBinding.clUsrInfo.setVisibility(View.GONE);
                            settingsFragmentExFragmentBinding.scrollviewUsrInfo.scrollTo(0, 0); // scrollView 回到顶部位置
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

            }
        });

//        settingsFragmentExFragmentBinding.btLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginViewModel.dbLogout();
//            }
//        });

        // 监视登录用户变动
        AppDatabase.getInstance(requireActivity()).myDao().getLoggedUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Log.e("SettingsFragmentEx", "loginViewModel.isLoggedIn = " + loginViewModel.isLoggedIn());

                    // 登录用户
                    if (!loginViewModel.isLoggedIn()) {
                        loginViewModel.setLoggedInUser(new LoggedInUser(user.userId, user.userName));
                    }

                    // 更新 SettingsFragmentExFragment UI
                    settingsFragmentExFragmentBinding.textView2UserName.setText(user.userName);
                    settingsFragmentExFragmentBinding.textView2UserID.setText(user.userId);

                    // 更新用户头像
                    if (user.userImage1 != null) {
                        File tmpJpg = new File(user.userImage1);
                        if (tmpJpg.length() > 0) {
                            settingsFragmentExFragmentBinding.imageViewUser.setImageBitmap(BitmapFactory.decodeFile(user.userImage1));
                        }
                    }

                } else {
                    Log.e("AppDataBaseObserve", "No LoggedUser!");

                    settingsFragmentExFragmentBinding.textView2UserName.setText("");
                    settingsFragmentExFragmentBinding.textView2UserID.setText("");
                    settingsFragmentExFragmentBinding.imageViewUser.setImageResource(R.mipmap.ic_launcher_round);
                }
            }
        });


    }

    /**
     * 还原 settingsFragmentEx 页面
     * */
    private void settingsFragmentExRecovery() {
        settingsFragmentExFragmentBinding.clUsrInfo.setVisibility(View.VISIBLE);
        settingsFragmentExFragmentBinding.scrollviewUsrInfo.scrollTo(0, 0); // scrollView 回到顶部位置

        settingsFragmentExFragmentBinding.fcvUsrInfo.setVisibility(View.GONE);
        settingsFragmentExFragmentBinding.fcvSettings.setVisibility(View.GONE);
        // ......
    }

}