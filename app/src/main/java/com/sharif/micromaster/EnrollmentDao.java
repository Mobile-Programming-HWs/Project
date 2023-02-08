package com.sharif.micromaster;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EnrollmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Enrollment enrollment);

    @Query("SELECT * FROM enrollment WHERE userId = :id")
    List<Enrollment> getUsersCourses(int id);

    @Query("SELECT * FROM enrollment WHERE userId = :userId AND courseId = :courseId")
    Enrollment getUserAndCourse(int userId, int courseId);
}
