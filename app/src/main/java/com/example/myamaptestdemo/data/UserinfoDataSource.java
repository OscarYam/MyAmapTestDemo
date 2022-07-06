package com.example.myamaptestdemo.data;


import com.alibaba.fastjson.JSONObject;
import com.example.myamaptestdemo.data.model.RequestData;
import com.example.myamaptestdemo.data.model.ResponResult;
import com.example.myamaptestdemo.data.model.UserEntity;
import com.example.myamaptestdemo.data.utility.utility;

import java.util.UUID;

public class UserinfoDataSource {

    /**
     * 更新远程数据库
     * */
    public Result<User> httpUpdateUser(User user) {
        try {
            // TODO: handle loggedInUser authentication
            RequestData rd = new RequestData();
            rd.setService("s.updateUser");

            rd.setUserToken(UUID.randomUUID().toString().replaceAll("-", ""));

            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(user.userId);
            userEntity.setUserName(user.userName);
            userEntity.setUserToken(user.userToken);
            userEntity.setUserPhone(user.userPhone);
            userEntity.setUserMailbox(user.userMailbox);
            userEntity.setUserImage1(user.userImage1);
            userEntity.setUserAge(user.userAge);
            userEntity.setUserSex(user.userSex);
            userEntity.setUserBirthday(user.userBirthday);
            rd.setUserEntity(userEntity);

            String httpRequestRet = utility.httpPostReq(utility.Encrypt_AES(utility.encodeData(rd)));
            ResponResult responResult = (ResponResult) utility.decodeData(httpRequestRet);

            if (responResult.getMessage().equals("OK")) {
                JSONObject jsonObj = (JSONObject) responResult.getData();

                User db_user = new User();
                db_user.userId = jsonObj.getString("userId");
                db_user.userName = jsonObj.getString("userName");
                db_user.userToken = jsonObj.getString("userToken");
                db_user.userPhone = jsonObj.getString("userPhone");
                db_user.userMailbox = jsonObj.getString("userMailbox");
                db_user.userLoginLastTime = jsonObj.getString("userLoginLastTime");
                db_user.userRepositoryTime = jsonObj.getString("userRepositoryTime");
                db_user.userAge = jsonObj.getInteger("userAge");
                db_user.userSex = jsonObj.getString("userSex");
                db_user.userBirthday = jsonObj.getString("userBirthday");


                return new Result.Success<>(db_user);
            } else {
                return new Result.Fail(responResult.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(e);
        }

    }


    /**
     * 获取用户所有数据
     * */
    public Result<User> httpGetUserByToken(String userId, String userToken) {
        try {
            // TODO: handle loggedInUser authentication
            RequestData rd = new RequestData();
            rd.setService("s.getUserByToken");

            rd.setUserToken(UUID.randomUUID().toString().replaceAll("-", ""));

            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(userId);
            userEntity.setUserToken(userToken);
            rd.setUserEntity(userEntity);

            String httpRequestRet = utility.httpPostReq(utility.Encrypt_AES(utility.encodeData(rd)));
            ResponResult responResult = (ResponResult) utility.decodeData(httpRequestRet);

            if (responResult.getMessage().equals("OK")) {
                JSONObject jsonObj = (JSONObject) responResult.getData();

                User db_user = new User();
                db_user.userId = jsonObj.getString("userId");
                db_user.userName = jsonObj.getString("userName");
                db_user.userToken = jsonObj.getString("userToken");
                db_user.userPhone = jsonObj.getString("userPhone");
                db_user.userMailbox = jsonObj.getString("userMailbox");
                db_user.userLoginLastTime = jsonObj.getString("userLoginLastTime");
                db_user.userRepositoryTime = jsonObj.getString("userRepositoryTime");
                db_user.userImage1 = jsonObj.getString("userImage1");
                db_user.userAge = jsonObj.getInteger("userAge");
                db_user.userSex = jsonObj.getString("userSex");
                db_user.userBirthday = jsonObj.getString("userBirthday");

                return new Result.Success<>(db_user);
            } else {
                return new Result.Fail(responResult.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(e);
        }

    }

}
