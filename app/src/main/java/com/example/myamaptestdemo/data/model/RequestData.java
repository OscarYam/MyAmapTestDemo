package com.example.myamaptestdemo.data.model;

public class RequestData {
    private String service;
    private String message;
    private String userName;
    private UserEntity userEntity;
    private String userId;
    private String userPsw;
    private String userToken;

    public RequestData() {}

    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPsw() {
        return userPsw;
    }
    public void setUserPsw(String userPsw) {
        this.userPsw = userPsw;
    }

    public String getUserToken() {
        return userToken;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
