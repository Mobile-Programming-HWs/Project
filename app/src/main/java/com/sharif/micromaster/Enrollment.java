package com.sharif.micromaster;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "enrollment")
public class Enrollment {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private int userId;

    @NonNull
    private int courseId;

    public Enrollment(int userId, int courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
