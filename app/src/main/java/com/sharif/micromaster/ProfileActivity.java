package com.sharif.micromaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView name;
    TextView email;
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
        User loggedIn = db.UserDao().getUserById(db.LoggedInUserDao().user().getUserID());
        Log.d("aaaaaa", String.valueOf(BitmapHelper.stringToBitmap(loggedIn.getImage())));
        //imageView.setImageBitmap(BitmapHelper.stringToBitmap(loggedIn.getImage()));
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
                Course course = db.CourseDao().getCourse(ta.getCourseId());
                courseList.add(course);
            }
        } else if (loggedIn.getUserType() == 2) {
            List<Enrollment> enrollments = db.EnrollmentDao().getUsersCourses(loggedIn.getId());
            for (Enrollment enrollment: enrollments) {
                Course course = db.CourseDao().getCourse(enrollment.getCourseId());
                courseList.add(course);
            }
        }
        logout.setOnClickListener(view -> {
            db.LoggedInUserDao().deleteAll();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
        adapter = new CustomAdapter(courseList, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    private void findViews() {
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        recyclerView = findViewById(R.id.profile_courses);
        recyclerView = findViewById(R.id.profile_courses);
        drawerLayout = findViewById(R.id.profile_drawer);
        navigationView = findViewById(R.id.nav_prof);
        logout = findViewById(R.id.logout);
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