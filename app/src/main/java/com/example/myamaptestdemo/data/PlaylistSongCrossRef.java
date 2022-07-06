package com.example.myamaptestdemo.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"playlistId", "songId"})
public class PlaylistSongCrossRef {

//    @ColumnInfo(index = true)
    @NonNull
    public String playlistId;

    @ColumnInfo(index = true)
    @NonNull
    public String songId;
}
