package com.sharif.micromaster;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class CourseDB {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private int teacherID;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private int units;

    public CourseDB(int teacherID, @NonNull String name, @NonNull String description, int units, @NonNull byte[] image) {
        this.teacherID = teacherID;
        this.name = name;
        this.description = description;
        this.units = units;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    @NonNull
    public byte[] getImage() {
        return image;
    }

    public void setImage(@NonNull byte[] image) {
        this.image = image;
    }

    @NonNull
    private byte[] image;
}
