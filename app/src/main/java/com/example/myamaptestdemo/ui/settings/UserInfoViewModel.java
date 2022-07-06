package com.example.myamaptestdemo.ui.settings;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myamaptestdemo.R;
import com.example.myamaptestdemo.data.Result;
import com.example.myamaptestdemo.data.User;
import com.example.myamaptestdemo.data.UserinfoDataSource;

import java.io.File;


public class UserInfoViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    // Create a LiveData with a String

    private MutableLiveData<Integer> frameVisibility;

    private MutableLiveData<String> userNickname;
    private MutableLiveData<String> userSex;
    private MutableLiveData<String> userBirthday;
    private MutableLiveData<String> userAddress;
    private MutableLiveData<NicknameFormState> nicknameFormState;
    private MutableLiveData<UpdateResult> updateResult;
    private MutableLiveData<Integer> userDataSync;
    private MutableLiveData<File> userTmpJpg;

    /**
     * 更新用户信息
     * */
    public static final int CHANGE_USERINFO = 0x01;
    /**
     * 更新用户数据
     * */
    public static final int CHANGE_USERDATA = 0x02;
    /**
     * 更新数据的类型
     * */
    private Integer changedDataType = null;

    public Integer getChangedDataType() {
        return changedDataType;
    }

    public void setChangedDataType(Integer changedDataType) {
        this.changedDataType = changedDataType;
    }

    public MutableLiveData<Integer> getFrameVisibility() {
        if (frameVisibility == null) {
            frameVisibility = new MutableLiveData<Integer>();
        }
        return frameVisibility;
    }


    public MutableLiveData<String> getUserNickname() {
        if (userNickname == null) {
            userNickname = new MutableLiveData<String>();
        }
        return userNickname;
    }

    public MutableLiveData<String> getUserSex() {
        if (userSex == null) {
            userSex = new MutableLiveData<String>();
        }
        return userSex;
    }

    public MutableLiveData<String> getUserBirthday() {
        if (userBirthday == null) {
            userBirthday = new MutableLiveData<String>();
        }
        return userBirthday;
    }

    public MutableLiveData<String> getUserAddress() {
        if (userAddress == null) {
            userAddress = new MutableLiveData<String>();
        }
        return userAddress;
    }

    public MutableLiveData<NicknameFormState> getNicknameFormState() {
        if (nicknameFormState == null) {
            nicknameFormState = new MutableLiveData<NicknameFormState>();
        }
        return nicknameFormState;
    }

    public MutableLiveData<UpdateResult> getUpdateResult() {
        if (updateResult == null) {
            updateResult = new MutableLiveData<UpdateResult>();
        }
        return updateResult;
    }

    public MutableLiveData<Integer> getUserDataSync() {
        if (userDataSync == null) {
            userDataSync = new MutableLiveData<Integer>();
        }
        return userDataSync;
    }

    public MutableLiveData<File> getUserTmpJpg() {
        if (userTmpJpg == null) {
            userTmpJpg = new MutableLiveData<File>();
        }
        return userTmpJpg;
    }

    public void nicknameDataChanged(String nicknameOld, String nicknameNew) {
        if (nicknameOld.matches(nicknameNew)) {
            nicknameFormState.setValue(new NicknameFormState("新昵称不能与原昵称相同"));
        } else {
            if (!isNicknameValid(nicknameNew)) {
                nicknameFormState.setValue(new NicknameFormState("Not a valid nickname"));
            } else {
                nicknameFormState.setValue(new NicknameFormState(true));
            }
        }
    }


    /**
     * 检查输入的昵称的格式合法性
     * */
    private boolean isNicknameValid(String nickname) {

        if (nickname == null || nickname.trim().length() < 5 || nickname.trim().length() > 20) {
            return false;
        } else {
            return !nickname.trim().isEmpty();
        }
    }


    /**
     * 更新用户远程数据库
     * */
    public void updateUser(User user) {
        UserinfoDataSource userinfoDataSource = new UserinfoDataSource();
        Result<User> result = userinfoDataSource.httpUpdateUser(user);
        if (result instanceof Result.Success) {
            User data = ((Result.Success<User>) result).getData();
            updateResult.postValue(new UpdateResult(data));
        } else if (result instanceof Result.Fail) {
            updateResult.postValue(new UpdateResult(((Result.Fail) result).getFail()));
        } else {
            updateResult.postValue(new UpdateResult(((Result.Error) result).getError().getMessage()));
        }
    }


    /**
     * 获取登录用户数据
     * */
    public User getUserByToken(String userId, String userToken) {
        UserinfoDataSource userinfoDataSource = new UserinfoDataSource();
        Result<User> result = userinfoDataSource.httpGetUserByToken(userId, userToken);
        if (result instanceof Result.Success) {
            return ((Result.Success<User>) result).getData();
        }
        if (result instanceof Result.Fail) {
            Log.e("UserInfoViewModel", ((Result.Fail) result).getFail());
            return null;
        }
        if (result instanceof Result.Error) {
            Log.e("UserInfoViewModel", ((Result.Error) result).getError().getMessage());
            return null;
        }
        return null;
    }


}