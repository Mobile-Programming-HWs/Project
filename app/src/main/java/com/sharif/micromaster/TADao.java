package com.sharif.micromaster;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TADao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(TA ta);

    @Query("SELECT * FROM ta WHERE userId = :id")
    List<TA> getTAByUserId(int id);

    @Query("SELECT * FROM ta WHERE courseId = :id")
    List<TA> getTAByCourseId(int id);

    @Query("SELECT * FROM ta WHERE id = :id")
    TA getTAById(int id);

    @Query("SELECT * FROM ta WHERE courseId = :courseId AND userId = :userId")
    TA getRelation(int courseId, int userId);

    @Delete
    void delete(TA ta);
}

