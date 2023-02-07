package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class CourseActivity extends AppCompatActivity {

    TextView name;
    TextView units;
    TextView teacher;
    TextView description;
    Button button;
    Course course;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        db = Database.getInstance(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        course = db.CourseDao().getCourse(id);
        findViews();
        User loggedIn = db.UserDao().getUserById(db.LoggedInUserDao().user().getUserID());
        if (loggedIn.getUserType() == 1) { // TA
            button.setText("Request to become TA");
        }
        if (loggedIn.getUserType() == 2) { // Student
            button.setText("Enroll");
        }
        name.setText(course.getName());
        teacher.setText(String.valueOf(db.UserDao().getUserById(course.getTeacherID()).getName()));
        units.setText(String.valueOf(course.getUnits()));
        description.setText(course.getDescription());
    }

    private void findViews() {
        name = findViewById(R.id.course_name);
        units = findViewById(R.id.course_units);
        teacher = findViewById(R.id.course_teacher);
        description = findViewById(R.id.course_description);
        button = findViewById(R.id.enroll);
    }
}