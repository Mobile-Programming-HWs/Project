package com.sharif.micromaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Update
    int update(User user);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUser(String email);

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(int id);
}
