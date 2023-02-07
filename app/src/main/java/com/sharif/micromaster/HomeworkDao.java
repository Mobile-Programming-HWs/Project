package com.sharif.micromaster;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HomeworkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Homework homework);

    @Delete
    void delete(Homework homework);

    @Query("SELECT * FROM homeworks WHERE courseID = :id")
    List<Homework> getHomeworksByCourseId(int id);
}
