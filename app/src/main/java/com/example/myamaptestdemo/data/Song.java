package com.example.myamaptestdemo.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {
    @PrimaryKey
    @NonNull
    public String songId;

    public String songName;

    public String artist;
}
