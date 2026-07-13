package com.sharif.micromaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
    TextView homeworkEmpty;
    Button button;
    Course course;
    Database db;
    RecyclerView recyclerView;
    List<Homework> homeworkList;
    RecyclerView.Adapter adapter;
    boolean canViewHomeworks = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        db = Database.getInstance(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        course = db.CourseDao().getCourse(id);
        findViews();
        LoggedInUser session = db.LoggedInUserDao().user();
        if (course == null || session == null) {
            Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        User loggedIn = db.UserDao().getUserById(session.getUserID());
        if (loggedIn == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (loggedIn.getUserType() == 0) { // Teacher
            if (course.getTeacherID() == loggedIn.getId()) {
                configureAddHomeworkButton();
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        }
        if (loggedIn.getUserType() == 1) { // TA
            TA ta = db.TADao().getRelation(course.getId(), loggedIn.getId());
            if (ta == null) {
                canViewHomeworks = false;
                showHomeworkMessage(R.string.empty_homeworks_ta_request);
                button.setText("Request to become TA");
                button.setOnClickListener(view -> {
                    long taId = db.TADao().insert(new TA(loggedIn.getId(), course.getId(), false));
                    if (taId == -1) {
                        Toast.makeText(this, "Request already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Request sent", Toast.LENGTH_SHORT).show();
                    }
                    showPendingTARequest();
                });
            } else if (ta.isApproved()) {
                configureAddHomeworkButton();
            } else {
                canViewHomeworks = false;
                showPendingTARequest();
            }
        }
        if (loggedIn.getUserType() == 2) { // Student
            Enrollment enrollment = db.EnrollmentDao().getUserAndCourse(loggedIn.getId(), course.getId());
            if (enrollment == null) {
                canViewHomeworks = false;
                button.setText("Enroll");
                showHomeworkMessage(R.string.empty_homeworks_enroll);
                button.setOnClickListener(view -> {
                    long enrollmentId = db.EnrollmentDao().insert(new Enrollment(loggedIn.getId(), course.getId()));
                    if (enrollmentId == -1) {
                        Toast.makeText(this, "Already enrolled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                    canViewHomeworks = true;
                    button.setVisibility(View.INVISIBLE);
                    updateHomeworkState();
                });
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        }
        name.setText(displayText(course.getName(), "Untitled course"));
        teacher.setText(getTeacherName(course.getTeacherID()));
        units.setText(course.getUnits() + " units");
        description.setText(displayText(course.getDescription(), "No description"));
        homeworkList = db.HomeworkDao().getHomeworksByCourseId(course.getId());
        adapter = new HomeworkAdapter(homeworkList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        updateHomeworkState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            List<Homework> newList = db.HomeworkDao().getHomeworksByCourseId(course.getId());
            if (newList.size() != homeworkList.size()) {
                homeworkList.clear();
                homeworkList.addAll(newList);
                adapter.notifyDataSetChanged();
                updateHomeworkState();
            }
        }
    }

    private void findViews() {
        name = findViewById(R.id.course_name);
        units = findViewById(R.id.course_units);
        teacher = findViewById(R.id.course_teacher);
        description = findViewById(R.id.course_description);
        homeworkEmpty = findViewById(R.id.course_homeworks_empty);
        button = findViewById(R.id.enroll);
        recyclerView = findViewById(R.id.course_homeworks);
    }

    private void configureAddHomeworkButton() {
        button.setText("Add homework");
        button.setEnabled(true);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(CourseActivity.this, AddHomeworkActivity.class);
            intent.putExtra("course", course.getId());
            startActivityForResult(intent, 100);
        });
    }

    private void showPendingTARequest() {
        showHomeworkMessage(R.string.empty_homeworks_ta_pending);
        button.setText("Request pending");
        button.setEnabled(false);
    }

    private void updateHomeworkState() {
        if (!canViewHomeworks) {
            return;
        }
        boolean hasHomeworks = homeworkList != null && !homeworkList.isEmpty();
        recyclerView.setVisibility(hasHomeworks ? View.VISIBLE : View.GONE);
        homeworkEmpty.setText(R.string.empty_homeworks);
        homeworkEmpty.setVisibility(hasHomeworks ? View.GONE : View.VISIBLE);
    }

    private void showHomeworkMessage(int messageResId) {
        recyclerView.setVisibility(View.GONE);
        homeworkEmpty.setText(messageResId);
        homeworkEmpty.setVisibility(View.VISIBLE);
    }

    private String getTeacherName(int teacherId) {
        User courseTeacher = db.UserDao().getUserById(teacherId);
        if (courseTeacher == null) {
            return "Unknown lecturer";
        }
        return displayText(courseTeacher.getName(), "Unknown lecturer");
    }

    private String displayText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }
}
