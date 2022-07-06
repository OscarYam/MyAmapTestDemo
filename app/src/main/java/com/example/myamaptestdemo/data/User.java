package com.example.myamaptestdemo.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String userId;
    public String userName;
    public String userMailbox;
    public String userPhone;
    public String userToken;
    public String userRepositoryTime;
    public String userLoginLastTime;
    public String userImage1;

    public Integer userAge;
    public String userSex;
    public String userBirthday;



    @ColumnInfo(name = "first_name")
    public String firstName;
    @ColumnInfo(name = "last_name")
    public String lastName;

    public String region;

    // 创建嵌套对象.可以使用 @Embedded 注释表示要分解为表格中的子字段的对象, 然后，您可以像查询其他各个列一样查询嵌套字段
    @Embedded
    public Address address;
}



//
///**
// * Immutable model class for a User
// */
//@Entity(tableName = "users")
//public class User {
//
//    @NonNull
//    @PrimaryKey
//    @ColumnInfo(name = "userid")
//    private String mId;
//
//    @ColumnInfo(name = "username")
//    private String mUserName;
//
//    @Ignore
//    public User(String userName) {
//        mId = UUID.randomUUID().toString();
//        mUserName = userName;
//    }
//
//    public User(@NonNull String id, String userName) {
//        this.mId = id;
//        this.mUserName = userName;
//    }
//
//    public String getId() {
//        return mId;
//    }
//
//    public String getUserName() {
//        return mUserName;
//    }
//}

