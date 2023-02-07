package com.sharif.micromaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(CourseDB courseDB);

    @Update
    int update(CourseDB courseDB);
}
