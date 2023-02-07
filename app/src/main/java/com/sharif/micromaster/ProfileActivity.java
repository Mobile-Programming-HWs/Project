package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView name;
    TextView email;
    RecyclerView recyclerView;
    List<Course> courseList;
    RecyclerView.Adapter adapter;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = Database.getInstance(this);
        User loggedIn = db.UserDao().getUserById(db.LoggedInUserDao().user().getUserID());
        name = findViewById(R.id.profile_name);
        name.setText(loggedIn.getName());
        email = findViewById(R.id.profile_email);
        email.setText(loggedIn.getEmail());
        recyclerView = findViewById(R.id.profile_courses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseList = new ArrayList<>();
        for (Course course: db.CourseDao().getCourses()) {
            if (course.getTeacherID() == loggedIn.getId()) {
                courseList.add(course);
            }
        }
        adapter = new CustomAdapter(courseList, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}