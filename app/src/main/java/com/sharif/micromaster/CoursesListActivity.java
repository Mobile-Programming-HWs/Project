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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class CoursesListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycler;
    TextView coursesEmpty;
    TextView coursesCount;
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
        coursesEmpty = findViewById(R.id.courses_empty);
        coursesCount = findViewById(R.id.courses_count);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomAdapter(courseList, this);
        recycler.setAdapter(adapter);
        updateCourseListState();
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

        User loggedIn = getLoggedInUser();
        boolean canAddCourse = loggedIn != null && loggedIn.getUserType() == 0;
        fab.setVisibility(canAddCourse ? View.VISIBLE : View.GONE);
        fab.setOnClickListener(view -> {
            User currentUser = getLoggedInUser();
            if (currentUser != null && currentUser.getUserType() == 0) {
                Intent intent = new Intent(CoursesListActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, 11);
            } else {
                Toast.makeText(CoursesListActivity.this, "You have to be a teacher to add a course!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            List<Course> newCourses = db.CourseDao().getCourses();
            if (newCourses.size() != courseList.size()) {
                courseList.add(newCourses.get(newCourses.size() - 1));
                adapter.notifyItemInserted(newCourses.size() - 1);
                updateCourseListState();
            }
        }
    }

    private void updateCourseListState() {
        boolean hasCourses = courseList != null && !courseList.isEmpty();
        int count = courseList == null ? 0 : courseList.size();
        coursesCount.setText(getString(R.string.courses_count_format, count));
        recycler.setVisibility(hasCourses ? View.VISIBLE : View.GONE);
        coursesEmpty.setVisibility(hasCourses ? View.GONE : View.VISIBLE);
    }

    private User getLoggedInUser() {
        LoggedInUser session = db.LoggedInUserDao().user();
        if (session == null) {
            return null;
        }
        return db.UserDao().getUserById(session.getUserID());
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
