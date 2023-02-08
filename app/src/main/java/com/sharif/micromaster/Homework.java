package com.sharif.micromaster;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "homeworks")
public class Homework {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private int courseID;

    @NonNull
    private int creator;

    @NonNull
    private String pdfLink;

    private String description;

    public Homework(int courseID, int creator, String description, @NonNull String pdfLink) {
        this.id = id;
        this.courseID = courseID;
        this.creator = creator;
        this.description = description;
        this.pdfLink = pdfLink;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator( int creator) {
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }
}
