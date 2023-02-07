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

    private String description;
    private String pdfLinks;

    public Homework(int courseID, String description, String pdfLinks) {
        this.courseID = courseID;
        this.description = description;
        this.pdfLinks = pdfLinks;
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

    public String getPdfLinks() {
        return pdfLinks;
    }

    public void setPdfLinks(String pdfLinks) {
        this.pdfLinks = pdfLinks;
    }
}
