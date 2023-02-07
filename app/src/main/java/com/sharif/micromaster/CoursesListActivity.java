package com.sharif.micromaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class CoursesListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycler;
    List<Course> courseList;
    RecyclerView.Adapter adapter;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Database db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);
        db = Database.getInstance(this);
        courseList = db.CourseDao().getCourses();
        fab = findViewById(R.id.floatingActionButton);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            User loggedIn = db.UserDao().getUserById(db.LoggedInUserDao().user().getUserID());
            if (loggedIn.getUserType() == 0) {
                Intent intent = new Intent(this, AddCourseActivity.class);
                startActivityForResult(intent, 11);
            } else {
                Toast.makeText(CoursesListActivity.this, "You have to be a teacher to add a course!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 11) {
            List<Course> newCourses = db.CourseDao().getCourses();
            if (newCourses.size() != courseList.size()) {
                courseList.add(newCourses.get(newCourses.size() - 1));
                adapter.notifyItemInserted(newCourses.size() - 1);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item2) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            navigationView.setCheckedItem(R.id.item1);
        }
        return true;
    }
}
