package com.sharif.micromaster;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ta")
public class TA {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private int userId;

    @NonNull
    private int courseId;

    private boolean isApproved;

    public TA(int userId, int courseId, boolean isApproved) {
        this.userId = userId;
        this.courseId = courseId;
        this.isApproved = isApproved;
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

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
