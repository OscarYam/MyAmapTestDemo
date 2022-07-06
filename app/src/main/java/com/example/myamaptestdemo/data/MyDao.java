package com.example.myamaptestdemo.data;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(User... users);

    @Insert
    public void insertBothUsers(User user1, User user2);

    @Insert
    public void insertUsersAndFriends(User user, List<User> friends);

    @Update
    public void updateUsers(User... users);

    @Query("UPDATE user SET userToken=:userToken WHERE userId=:userId")
    public void setUserToken(String userId, String userToken);

    @Delete
    public void deleteUsers(User... users);

    @Query("SELECT * FROM user")
    public User[] loadAllUsers();

    @Query("SELECT * FROM user WHERE userAge > :minAge")
    public User[] loadAllUsersOlderThan(int minAge);

    @Query("SELECT * FROM user WHERE userAge BETWEEN :minAge AND :maxAge")
    public User[] loadAllUsersBetweenAges(int minAge, int maxAge);

    @Query("SELECT * FROM user WHERE first_name LIKE :search " +
            "OR last_name LIKE :search " +
            "OR userName LIKE :search ")
    public List<User> findUserWithName(String search);


    @Query("SELECT first_name, last_name FROM user")
    public List<NameTuple> loadFullName();

    class NameTuple {
        @ColumnInfo(name = "first_name")
        public String firstName;

        @ColumnInfo(name = "last_name")
        @NonNull
        public String lastName;
    }

    @Query("SELECT first_name, last_name FROM user WHERE region IN (:regions)")
    public List<NameTuple> loadUsersFromRegions(List<String> regions);





//    @Query("SELECT * FROM book " +
//            "INNER JOIN loan ON loan.book_id = book.id " +
//            "INNER JOIN user ON user.id = loan.user_id " +
//            "WHERE user.name LIKE :userName")
//    public List<Book> findBooksBorrowedByNameSync(String userName);
//
//    @Query("SELECT user.name AS userName, pet.name AS petName " +
//            "FROM user, pet " +
//            "WHERE user.id = pet.user_id")
//    public LiveData<List<UserPet>> loadUserAndPetNames();
//
//    // You can also define this class in a separate file, as long as you add the
//    // "public" access modifier.
//    static class UserPet {
//        public String userName;
//        public String petName;
//    }

    @Query("SELECT first_name, last_name FROM user WHERE region IN (:regions)")
    public LiveData<List<NameTuple>> loadUsersFromRegionsSync(List<String> regions);


    @Query("SELECT * FROM user WHERE userToken IS NOT NULL")
    public LiveData<User> getLoggedUser();

    @Query("SELECT * FROM user WHERE userToken IS NOT NULL")
    public User loadLoggedUsr();


    @Query("SELECT * FROM user WHERE userId = :id LIMIT 1")
    public List<User> loadUserById(String id);
//    public Flowable<User> loadUserById(String id);

    @Query("SELECT * FROM user WHERE userToken IS NOT NULL")
    public Flowable<User> loadLoggedUser();

//    // Emits the number of users added to the database.
//    @Insert
//    public Maybe<Integer> insertLargeNumberOfUsers(List<User> users);

    // Makes sure that the operation finishes successfully.
    @Insert
    public Completable insertLargeNumberOfUsers(User... users);

    /* Emits the number of users removed from the database. Always emits at
       least one user. */
    @Delete
    public Single<Integer> deleteUsers(List<User> users);










    @Transaction
    @Query("SELECT * FROM User")
    public List<UserAndLibrary> getUsersAndLibraries();

    @Transaction
    @Query("SELECT * FROM User")
    public List<UserWithPlaylists> getUsersWithPlaylists();

    @Transaction
    @Query("SELECT * FROM Playlist")
    public List<PlaylistWithSongs> getPlaylistsWithSongs();

    @Transaction
    @Query("SELECT * FROM Song")
    public List<SongWithPlaylists> getSongsWithPlaylists();

    @Transaction
    @Query("SELECT * FROM User")
    public List<UserWithPlaylistsAndSongs> getUsersWithPlaylistsAndSongs();

}



///**
// * Data Access Object for the users table.
// */
//@Dao
//public interface UserDao {
//
//    /**
//     * Get the user from the table. Since for simplicity we only have one user in the database,
//     * this query gets all users from the table, but limits the result to just the 1st user.
//     *
//     * @return the user from the table
//     */
//    @Query("SELECT * FROM Users LIMIT 1")
//    Flowable<User> getUser();
//
//    /**
//     * Insert a user in the database. If the user already exists, replace it.
//     *
//     * @param user the user to be inserted.
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Completable insertUser(User user);
//
//    /**
//     * Delete all users.
//     */
//    @Query("DELETE FROM Users")
//    void deleteAllUsers();
//}
