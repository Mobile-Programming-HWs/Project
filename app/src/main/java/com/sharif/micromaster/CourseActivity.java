package com.sharif.micromaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CourseActivity extends AppCompatActivity {

    TextView name;
    TextView units;
    TextView teacher;
    TextView description;
    Button button;
    Course course;
    Database db;
    RecyclerView recyclerView;
    List<Homework> homeworkList;
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        db = Database.getInstance(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        course = db.CourseDao().getCourse(id);
        findViews();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        User loggedIn = db.UserDao().getUserById(db.LoggedInUserDao().user().getUserID());
        if (loggedIn.getUserType() == 0) { // Teacher
            if (course.getTeacherID() == loggedIn.getId()) {
                button.setText("Add homework");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(CourseActivity.this, AddHomeworkActivity.class);
                        intent1.putExtra("course", course.getId());
                        startActivityForResult(intent1, 100);
                    }
                });
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        }
        if (loggedIn.getUserType() == 1) { // TA
            TA ta = db.TADao().getRelation(course.getId(), loggedIn.getId());
            if (ta == null) {
                recyclerView.setVisibility(View.INVISIBLE);
                button.setText("Request to become TA");
                button.setOnClickListener(view -> {
                    db.TADao().insert(new TA(loggedIn.getId(), course.getId(), true));
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                    button.setText("Add homework");
                    recyclerView.setVisibility(View.VISIBLE);
                    button.setOnClickListener(view2 -> {
                        Intent intent1 = new Intent(CourseActivity.this, AddHomeworkActivity.class);
                        intent1.putExtra("course", course.getId());
                        startActivityForResult(intent1, 100);
                    });
                });
            } else {
                button.setText("Add homework");
                button.setOnClickListener(view -> {
                    Intent intent1 = new Intent(this, AddHomeworkActivity.class);
                    intent1.putExtra("course", course.getId());
                    startActivityForResult(intent1, 100);
                });
            }
        }
        if (loggedIn.getUserType() == 2) { // Student
            Enrollment enrollment = db.EnrollmentDao().getUserAndCourse(loggedIn.getId(), course.getId());
            if (enrollment == null) {
                button.setText("Enroll");
                recyclerView.setVisibility(View.INVISIBLE);
                button.setOnClickListener(view -> {
                    db.EnrollmentDao().insert(new Enrollment(loggedIn.getId(), course.getId()));
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                });
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        }
        name.setText(course.getName());
        teacher.setText(String.valueOf(db.UserDao().getUserById(course.getTeacherID()).getName()));
        units.setText(String.valueOf(course.getUnits()));
        description.setText(course.getDescription());
        homeworkList = db.HomeworkDao().getHomeworksByCourseId(course.getId());
        adapter = new HomeworkAdapter(homeworkList, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            List<Homework> newList = db.HomeworkDao().getHomeworksByCourseId(course.getId());
            if (newList.size() != homeworkList.size()) {
                homeworkList.add(newList.get(newList.size() - 1));
                adapter.notifyItemInserted(homeworkList.size() - 1);
            }
        }
    }

    private void findViews() {
        name = findViewById(R.id.course_name);
        units = findViewById(R.id.course_units);
        teacher = findViewById(R.id.course_teacher);
        description = findViewById(R.id.course_description);
        button = findViewById(R.id.enroll);
        recyclerView = findViewById(R.id.course_homeworks);
    }
}