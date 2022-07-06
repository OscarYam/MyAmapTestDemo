package com.example.myamaptestdemo.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Library {
    @PrimaryKey
    @NonNull
    public String libraryId;

    public String userOwnerId;
}
