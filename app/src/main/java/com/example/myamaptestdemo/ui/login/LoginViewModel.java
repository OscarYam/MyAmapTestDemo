package com.example.myamaptestdemo.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myamaptestdemo.data.AppDatabase;
import com.example.myamaptestdemo.data.LoginRepository;
import com.example.myamaptestdemo.data.Result;
import com.example.myamaptestdemo.data.User;
import com.example.myamaptestdemo.data.model.LoggedInUser;
import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.data.utility.utility;
import com.example.myamaptestdemo.util.Patterns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;
    private MutableLiveData<Integer> frameVisibility;

    /**
     * 外部存储空间中的应用专属图片目录
     * */
    private File userImageExternalFilesDir = null;

    public File getUserImageExternalFilesDir() {
        return userImageExternalFilesDir;
    }
    public void setUserImageExternalFilesDir(File userImageExternalFilesDir) {
        this.userImageExternalFilesDir = userImageExternalFilesDir;
    }


    public MutableLiveData<Integer> getFrameVisibility() {
        if (frameVisibility == null) {
            frameVisibility = new MutableLiveData<Integer>();
        }
        return frameVisibility;
    }

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public boolean isLoggedIn() {
        return loginRepository.isLoggedIn();
    }

    public void setLoggedInUser(LoggedInUser loggedInUser) {

//        Log.e("LoginViewModel", "userId = " + loggedInUser.getUserId() + " isLoggedIn = " + isLoggedIn());

        if (!isLoggedIn()) {
            loginRepository.setLoggedInUser(loggedInUser);
            loginResult.postValue(new LoginResult(new LoggedInUserView(loggedInUser.getDisplayName(), loggedInUser.getUserId())));
        }
    }

    private void logout() {
        if (loginRepository.isLoggedIn()) {
            loginRepository.logout();
            loginResult.postValue(new LoginResult("Logout succeed!"));
        } else {
            loginResult.postValue(new LoginResult("请先登录"));
        }
    }

    public void dbLogout() {
        loginRepository.dbLogout();
        logout();
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<User> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            User user = ((Result.Success<User>) result).getData();


            // 存储头像, 更新本地数据库
            if (userImageExternalFilesDir != null) {
                updateUserData(userImageExternalFilesDir, user);
                loginResult.postValue(new LoginResult(new LoggedInUserView(user.userName, user.userId)));
            } else {
                Log.e("LoginViewModel", "userImageExternalFilesDir = null");
            }

        } else if (result instanceof Result.Fail) {
            loginResult.postValue(new LoginResult(((Result.Fail) result).getFail()));
        } else {
            loginResult.postValue(new LoginResult(((Result.Error) result).getError().getMessage()));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        } else if (username.contains("@")) {
            // 邮箱验证
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


    /**
     * 更新本地用户数据(用户信息、头像图片...)
     * */
    public void updateUserData(@NonNull File userImageExternalFilesDir, @NonNull User user) {
        // Get the pictures directory that's inside the app-specific directory on external storage.
        File tmpJpg = new File(userImageExternalFilesDir, user.userId);
        if (!tmpJpg.exists()) {
            if (!tmpJpg.mkdirs()) {
                Log.e("LoginViewModel", "Directory not created");
                loginResult.postValue(new LoginResult("System error: Directory not created"));
                return;
            }
        }

        if (user.userImage1 != null) {
            Log.e("LoginViewModel", "user.userImage1.length() = " + user.userImage1.length() + " (bytes)");

            if (user.userImage1.getBytes().length > 0) {
                try {
                    tmpJpg = new File(tmpJpg.getPath(), "userImage1.jpg");

                    // BASE64 解码
                    byte[] bytes = utility.Decode_BASE64(user.userImage1);
                    OutputStream out = new FileOutputStream(tmpJpg);
                    out.write(bytes);
                    out.flush();
                    out.close();

                    Log.e("LoginViewModel", "bytes.length = " + bytes.length + " (bytes)");

                    user.userImage1 = tmpJpg.getPath();

                } catch (Exception e) {
                    Log.e("LoginViewModel", e.getMessage());

                    user.userImage1 = null;
                }
            } else {
                user.userImage1 = null;
            }
        }

        // 更新本地数据库
        AppDatabase.getInstance().myDao().insertUsers(user);
    }
}