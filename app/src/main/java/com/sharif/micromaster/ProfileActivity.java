package com.sharif.micromaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView name;
    TextView email;
    TextView coursesEmpty;
    RecyclerView recyclerView;
    de.hdodenhof.circleimageview.CircleImageView imageView;
    List<Course> courseList;
    RecyclerView.Adapter adapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Database db;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = Database.getInstance(this);
        findViews();
        navigationView.getMenu().getItem(1).setChecked(true);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.item2);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LoggedInUser session = db.LoggedInUserDao().user();
        if (session == null) {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
            goToLogin();
            return;
        }
        User loggedIn = db.UserDao().getUserById(session.getUserID());
        if (loggedIn == null) {
            db.LoggedInUserDao().deleteAll();
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            goToLogin();
            return;
        }
        if (loggedIn.getImage() != null && !loggedIn.getImage().isEmpty()) {
            imageView.setImageBitmap(BitmapHelper.stringToBitmap(loggedIn.getImage()));
        }
        name.setText(loggedIn.getName());
        email.setText(loggedIn.getEmail());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseList = new ArrayList<>();
        if (loggedIn.getUserType() == 0) {
            for (Course course : db.CourseDao().getCourses()) {
                if (course.getTeacherID() == loggedIn.getId()) {
                    courseList.add(course);
                }
            }
        } else if (loggedIn.getUserType() == 1) {
            List<TA> taArrayList = db.TADao().getTAByUserId(loggedIn.getId());
            for (TA ta: taArrayList) {
                if (!ta.isApproved()) {
                    continue;
                }
                Course course = db.CourseDao().getCourse(ta.getCourseId());
                if (course != null) {
                    courseList.add(course);
                }
            }
        } else if (loggedIn.getUserType() == 2) {
            List<Enrollment> enrollments = db.EnrollmentDao().getUsersCourses(loggedIn.getId());
            for (Enrollment enrollment: enrollments) {
                Course course = db.CourseDao().getCourse(enrollment.getCourseId());
                if (course != null) {
                    courseList.add(course);
                }
            }
        }
        logout.setOnClickListener(view -> {
            db.LoggedInUserDao().deleteAll();
            goToLogin();
        });
        adapter = new CustomAdapter(courseList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        updateCourseListState();
    }

    private void findViews() {
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        coursesEmpty = findViewById(R.id.profile_courses_empty);
        recyclerView = findViewById(R.id.profile_courses);
        imageView = findViewById(R.id.profile_image);
        drawerLayout = findViewById(R.id.profile_drawer);
        navigationView = findViewById(R.id.nav_prof);
        logout = findViewById(R.id.logout);
    }

    private void updateCourseListState() {
        boolean hasCourses = courseList != null && !courseList.isEmpty();
        recyclerView.setVisibility(hasCourses ? View.VISIBLE : View.GONE);
        coursesEmpty.setVisibility(hasCourses ? View.GONE : View.VISIBLE);
    }

    private void goToLogin() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
        if (id == R.id.item1) {
            finish();
        }
        return true;
    }
}
