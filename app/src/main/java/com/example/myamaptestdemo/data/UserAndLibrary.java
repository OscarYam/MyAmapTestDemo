package com.example.myamaptestdemo.data;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserAndLibrary {
    @Embedded
    public User user;

    @Relation(
            parentColumn = "userId",
            entityColumn = "userOwnerId"
    )

    public Library library;
}
