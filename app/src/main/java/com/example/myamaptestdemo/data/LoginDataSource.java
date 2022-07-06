package com.example.myamaptestdemo.data;

import com.alibaba.fastjson.JSONObject;
import com.example.myamaptestdemo.data.model.RequestData;
import com.example.myamaptestdemo.data.model.ResponResult;
import com.example.myamaptestdemo.data.utility.utility;

import java.util.UUID;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<User> login(String username, String password) {
        try {
            // TODO: handle loggedInUser authentication
            RequestData rd = new RequestData();
            rd.setService("s.userLogin");
            rd.setUserId(username);
            rd.setUserPsw(password);

            rd.setUserToken(UUID.randomUUID().toString().replaceAll("-", ""));

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

    public void logout() {
        // TODO: revoke authentication
    }
}