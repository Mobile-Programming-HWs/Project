package com.sharif.micromaster;

import java.util.ArrayList;

public class Course {
    public static ArrayList<Course> courses = new ArrayList<>();
    static int instances = 0;

    int id, image;
    String name, lecturer, description, units;

    public Course() {
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public Course(int image, String units, String name, String description, String lecturer) {
        this.image = image;
        this.units = units;
        this.name = name;
        this.description = description;
        this.lecturer = lecturer;
        this.id = instances;
        instances++;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}