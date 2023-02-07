package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CoursesListActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<Course> courseList;
    RecyclerView.Adapter adapter;
    FloatingActionButton fab;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);
        db = Database.getInstance(this);
        courseList = db.CourseDao().getCourses();
        fab = findViewById(R.id.floatingActionButton);
        recycler = findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        adapter = new CustomAdapter(courseList, getApplicationContext());
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recycler, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(CoursesListActivity.this, CourseActivity.class);
                        intent.putExtra("id", courseList.get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCourseActivity.class);
            startActivity(intent);
            courseList = db.CourseDao().getCourses();
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        courseList = db.CourseDao().getCourses();
        adapter.notifyDataSetChanged();
    }
}