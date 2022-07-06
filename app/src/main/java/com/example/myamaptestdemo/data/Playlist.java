package com.example.myamaptestdemo.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Playlist {
    @PrimaryKey
    @NonNull
    public String playlistId;

    public String userCreatorId;

    public String playlistName;
}
