package com.example.myamaptestdemo.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithPlaylistsAndSongs {
    @Embedded
    public User user;

    @Relation(
            entity = Playlist.class,
            parentColumn = "userId",
            entityColumn = "userCreatorId"
    )

    public List<PlaylistWithSongs> playlists;
}
