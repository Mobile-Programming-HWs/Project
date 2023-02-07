package com.sharif.micromaster;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface HomeworkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Homework homework);

    @Delete
    void delete(Homework homework);

}
