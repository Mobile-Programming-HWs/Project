package com.sharif.micromaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Course course);

    @Update
    int update(Course course);

    @Query("SELECT * FROM courses")
    List<Course> getCourses();

    @Query("SELECT * FROM courses WHERE id = :id")
    Course getCourse(int id);
}
