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
import android.widget.Toast;

import com.example.myamaptestdemo.MainActivityViewModel;
import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.Utils;
import com.example.myamaptestdemo.data.AppDatabase;
import com.example.myamaptestdemo.data.User;
import com.example.myamaptestdemo.data.utility.utility;
import com.example.myamaptestdemo.databinding.UserInfoFragmentBinding;
import com.example.myamaptestdemo.ui.login.LoginResult;
import com.example.myamaptestdemo.ui.login.LoginViewModel;
import com.example.myamaptestdemo.ui.login.LoginViewModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class UserInfoFragment extends Fragment {

    private static final String TARGET = "UserInfoFragment";

    private UserInfoViewModel userInfoViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private SettingsFragmentExViewModel settingsFragmentExViewModel;
    private LoginViewModel loginViewModel;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    private UserInfoFragmentBinding userInfoFragmentBinding;
    private File tmpJpg = null;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        userInfoFragmentBinding = UserInfoFragmentBinding.inflate(inflater, container, false);

        return userInfoFragmentBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        settingsFragmentExViewModel = new ViewModelProvider(requireActivity()).get(SettingsFragmentExViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity(), new LoginViewModelFactory()).get(LoginViewModel.class);

        // TODO: Use the ViewModel

        // 监视本地数据库登录用户数据变动
        AppDatabase.getInstance(requireActivity()).myDao().getLoggedUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    // 设置当前用户头像临时文件
                    setTmpJpg(user.userId);

                    // 更新用户 UI
                    // 更新用户名
                    userInfoFragmentBinding.tvNickName.setText(user.userName);

                    // 更新用户头像
                    if (user.userImage1 != null) {
                        File mTmpJpg = new File(user.userImage1);
                        if (mTmpJpg.length() > 0) {
                            userInfoFragmentBinding.imageViewUser.setImageBitmap(BitmapFactory.decodeFile(user.userImage1));
                        } else {
                            // 文件缺失
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    User user_New = userInfoViewModel.getUserByToken(user.userId, user.userToken);
                                    if (user_New != null) {
                                        loginViewModel.updateUserData(loginViewModel.getUserImageExternalFilesDir(), user_New);
                                    } else {
                                        mainActivityViewModel.getError().postValue("必要文件缺失!");
                                        loginViewModel.dbLogout();
//                                        Log.e("UserInfoFragment", "user_New = null");
                                    }
                                }
                            }).start();
                        }
                    }

                    // 更新用户性别
                    userInfoFragmentBinding.tvSex.setText(user.userSex);

                    // 更新用户生日
                    userInfoFragmentBinding.tvBirthday.setText(user.userBirthday);

                    // ......

                } else {
                    // No LoggedUser
                    userInfoFragmentBinding.tvNickName.setText("");
                    userInfoFragmentBinding.imageViewUser.setImageResource(R.mipmap.ic_launcher_round);
                }
            }
        });

        userInfoViewModel.getUpdateResult().observe(requireActivity(), new Observer<UpdateResult>() {
            @Override
            public void onChanged(UpdateResult updateResult) {
                if (updateResult == null) {
                    return;
                }

                // 数据更新失败
                if (updateResult.getError() != null) {
                    if (getContext() != null && getContext().getApplicationContext() != null) {
                        Toast.makeText(getContext().getApplicationContext(), "数据更新失败!", Toast.LENGTH_LONG).show();

                        if (updateResult.getError().matches("用户Token错误")) {
                            Toast.makeText(getContext().getApplicationContext(), "身份验证信息错误!", Toast.LENGTH_LONG).show();

                            loginViewModel.dbLogout();
                            return;
                        } else {
                            Toast.makeText(getContext().getApplicationContext(), updateResult.getError(), Toast.LENGTH_LONG).show();
                        }


                        if (tmpJpg.exists()) {
                            tmpJpg.delete();
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                User user = AppDatabase.getInstance().myDao().loadLoggedUsr();
                                // 刷新本地数据库还原所有 UI
                                AppDatabase.getInstance().myDao().insertUsers(user);
                            }
                        }).start();

                    }
                }

                // 数据更新成功
                if (updateResult.getSuccess() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User user = updateResult.getSuccess();

                            // 不需要更新头像路径
                            user.userImage1 = AppDatabase.getInstance().myDao().loadLoggedUsr().userImage1;
                            if (user.userImage1 == null) {
                                // 若头像未设置,则创建头像储存路径
                                File tmpJpg = new File(loginViewModel.getUserImageExternalFilesDir(), user.userId);
                                if (!tmpJpg.exists()) {
                                    if (!tmpJpg.mkdirs()) {
                                        Log.e(TARGET, "Directory not created!");
                                        mainActivityViewModel.getError().postValue("System error: Directory not created");
                                        return;
                                    }
                                }
                                user.userImage1 = new File(tmpJpg.getPath(), "userImage1.jpg").getPath();
                            }

                            // 将temp.jpg 重命名为 userImage1.jpg (该处理方法有些手机会出现缩略图和内容不符的bug)
                            if (tmpJpg.exists()) {
                                File usrImage1 = new File(user.userImage1);
                                if (usrImage1.delete()) {
                                    if (!tmpJpg.renameTo(usrImage1)) {
                                        Log.e("UserInfoFragment", "tmpJpg.jpg rename failed !");
                                    }
                                } else {
                                    Log.e("UserInfoFragment", "userImage1.jpg delete failed !");
                                }
                                tmpJpg.delete();
                            }

                            // 更新本地数据库
                            AppDatabase.getInstance().myDao().insertUsers(user);
                        }
                    }).start();

                    if (getContext() != null && getContext().getApplicationContext() != null) {
                        Toast.makeText(getContext().getApplicationContext(), "数据更新成功!", Toast.LENGTH_LONG).show();

//                        userinfoFragmentRecovery();
                    }
                }
            }
        });


        userInfoFragmentBinding.llUsrInfoSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("userinfo","frame onTouch");
                if (Utils.back2PreviousFrame(motionEvent)) {
                    // 隐藏本层 frame, 并显示上一层 frame
//                    Log.e("userinfo", "返回上一层 frame");
                    userInfoViewModel.getFrameVisibility().postValue(View.GONE);
                    settingsFragmentExViewModel.getClUsrInfoVisibility().postValue(View.VISIBLE);
                }
                return true;
            }
        });

        userInfoFragmentBinding.imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 修改头像
                ChangeUserImageDialogFragment changeUserImageDialogFragment = new ChangeUserImageDialogFragment();
                changeUserImageDialogFragment.setOnUserImageSetListener(new ChangeUserImageDialogFragment.OnUserImageSetListener() {
                    @Override
                    public void onUserImageSet(File userImage) {
//                        if (getContext() != null && getContext().getApplicationContext() != null) {
//                            Toast.makeText(getContext().getApplicationContext(), "userImg = " + userImage.getPath(), Toast.LENGTH_LONG).show();
//                        }

                        // 更新远程数据库, 同步 Userinfo 数据
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 获取当前登录用户
                                User db_user = AppDatabase.getInstance().myDao().loadLoggedUsr();
                                if (db_user != null) {
                                    User newUser = new User();
                                    newUser.userId = db_user.userId;
                                    newUser.userToken = db_user.userToken;

                                    Log.e("UserInfoFragment", "tmpJpg.length() = " + userImage.length() + " (bytes)");

                                    // 读取图片字节数组
                                    try {
                                        InputStream in = new FileInputStream(userImage);
                                        byte[] data = new byte[in.available()];
                                        in.read(data);
                                        in.close();

                                        // BASE64 转码
                                        String str = utility.Encode_BASE64(data);
                                        newUser.userImage1 = str;
                                        Log.e("UserInfoFragment", "str.length() = " + str.length() + " (bytes)");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    // 更新远程数据库
                                    userInfoViewModel.updateUser(newUser);
                                }
                            }
                        }).start();

                    }
                });
                changeUserImageDialogFragment.show(getParentFragmentManager(), "ChangeUserImageDialogFragment");
            }
        });

        userInfoFragmentBinding.llNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 修改昵称
                ChangeNicknameDialogFragment changeNicknameDialogFragment = new ChangeNicknameDialogFragment();
                changeNicknameDialogFragment.setOnNicknameSetListener(new ChangeNicknameDialogFragment.OnNicknameSetListener() {
                    @Override
                    public void onNicknameSet(String nickname) {
//                        if (getContext() != null && getContext().getApplicationContext() != null) {
//                            Toast.makeText(getContext().getApplicationContext(), "newNickname = " + nickname, Toast.LENGTH_SHORT).show();
//                        }

                        // 更新远程数据库, 同步 Userinfo 数据
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 获取当前登录用户
                                User db_user = AppDatabase.getInstance().myDao().loadLoggedUsr();
                                if (db_user != null) {
                                    User newUser = new User();
                                    newUser.userId = db_user.userId;
                                    newUser.userToken = db_user.userToken;

                                    newUser.userName = nickname;

                                    // 更新远程数据库
                                    userInfoViewModel.updateUser(newUser);
                                }
                            }
                        }).start();

                    }
                });
                changeNicknameDialogFragment.show(getParentFragmentManager(), "ChangeNicknameDialogFragment");
            }
        });

        userInfoFragmentBinding.llSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 修改性别
                ChangeSexDialogFragment changeSexDialogFragment = new ChangeSexDialogFragment();
                changeSexDialogFragment.setOnSexChooseListener(new ChangeSexDialogFragment.OnSexChooseListener() {
                    @Override
                    public void onSexChoose(String sex) {
//                        if (getContext() != null && getContext().getApplicationContext() != null) {
//                            Toast.makeText(getContext().getApplicationContext(), "sex = " + sex, Toast.LENGTH_SHORT).show();
//                        }

                        // 更新远程数据库, 同步 Userinfo 数据
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 获取当前登录用户
                                User db_user = AppDatabase.getInstance().myDao().loadLoggedUsr();
                                if (db_user != null) {
                                    User newUser = new User();
                                    newUser.userId = db_user.userId;
                                    newUser.userToken = db_user.userToken;

                                    if (db_user.userSex == null || !db_user.userSex.matches(sex)) {
                                        newUser.userSex = sex;
                                        // 更新远程数据库
                                        userInfoViewModel.updateUser(newUser);
                                    }

                                }
                            }
                        }).start();
                    }
                });
                changeSexDialogFragment.show(getParentFragmentManager(), "ChangeSexDialogFragment");
            }
        });

        userInfoFragmentBinding.llBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 修改生日
                ChangeBirthdayDialogFragment changeBirthdayDialogFragment = new ChangeBirthdayDialogFragment();
                changeBirthdayDialogFragment.setOnDateChooseListener(new ChangeBirthdayDialogFragment.OnDateChooseListener() {
                    @Override
                    public void onDateChoose(int year, int month, int day) {
//                        if (getContext() != null && getContext().getApplicationContext() != null) {
//                            Toast.makeText(getContext().getApplicationContext(), year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
//                        }

                        StringBuilder birthday = new StringBuilder();
                        birthday.append(year).append("-").append(month > 9 ? month : "0" + month).append("-").append(day > 9 ? day : "0" + day);

                        // 更新远程数据库, 同步 Userinfo 数据
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 获取当前登录用户
                                User db_user = AppDatabase.getInstance().myDao().loadLoggedUsr();
                                if (db_user != null) {
                                    User newUser = new User();
                                    newUser.userId = db_user.userId;
                                    newUser.userToken = db_user.userToken;

                                    if (db_user.userBirthday == null || !db_user.userBirthday.matches(birthday.toString())) {
                                        newUser.userBirthday = birthday.toString();
                                        // 更新远程数据库
                                        userInfoViewModel.updateUser(newUser);
                                    }

                                }
                            }
                        }).start();


                    }
                });
                changeBirthdayDialogFragment.show(getParentFragmentManager(), "ChangeBirthdayDialogFragment");
            }
        });

        userInfoFragmentBinding.llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 修改地址
                ChangeAddressDialogFragment changeAddressDialogFragment = new ChangeAddressDialogFragment();
                changeAddressDialogFragment.setOnAddressSetListener(new ChangeAddressDialogFragment.OnAddressSetListener() {
                    @Override
                    public void onAddressSet(String address) {
                        Log.e(TARGET, address);
                        userInfoFragmentBinding.tvAddress.setText(address);
                    }
                });
                changeAddressDialogFragment.show(getParentFragmentManager(), "ChangeAddressDialogFragment");

            }
        });

        userInfoFragmentBinding.llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 修改隐私设置

            }
        });

    }

    /**
     * 设置当前用户头像临时文件路径
     * */
    private void setTmpJpg(String userId) {
//        tmpJpg = Utils.getAppSpecificAlbumStorageDir(requireActivity(), userId);
        tmpJpg = new File(loginViewModel.getUserImageExternalFilesDir(), userId);
        tmpJpg = new File(tmpJpg.getPath(), "temp.jpg");

        userInfoViewModel.getUserTmpJpg().postValue(tmpJpg);

        if (tmpJpg.exists()) {
            tmpJpg.delete();
        }
    }

}