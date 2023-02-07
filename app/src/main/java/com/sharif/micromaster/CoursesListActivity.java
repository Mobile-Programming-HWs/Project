package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CoursesListActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<Course> courseList;
    RecyclerView.Adapter adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);
        fab = findViewById(R.id.floatingActionButton);
        courseList = Course.courses;
        recycler = findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        courseList.add(new Course(R.drawable.ic_launcher_foreground, "4", "Quantom", "very good course", "Ali alavi"));
        courseList.add(new Course(R.drawable.ic_launcher_background, "3", "Dynamic", "horrible course", "Shalil Shalavi"));
        courseList.add(new Course(R.drawable.ic_launcher_foreground, "2", "Static", "no cm", "abbas agha"));
        courseList.add(new Course(R.drawable.ic_launcher_foreground, "4", "Quantom2", "very good course2", "Ali alavi"));
        courseList.add(new Course(R.drawable.ic_launcher_background, "3", "Dynamic2", "horrible course2", "Shalil Shalavi"));
        courseList.add(new Course(R.drawable.ic_launcher_foreground, "2", "Static2", "no cm2", "abbas agha"));

        adapter = new CustomAdapter(courseList, getApplicationContext());
        recycler.setAdapter(adapter);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCourseActivity.class);
            startActivity(intent);
            adapter.notifyDataSetChanged();
        });
    }
}