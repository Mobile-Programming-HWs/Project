package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddHomeworkActivity extends AppCompatActivity {

    TextView creator;
    EditText description;
    EditText link;
    Button submit;
    Database db;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);
        findViews();
        db = Database.getInstance(this);
        Intent intent = getIntent();
        int courseId = intent.getIntExtra("course", -1);
        course = db.CourseDao().getCourse(courseId);
        LoggedInUser session = db.LoggedInUserDao().user();
        if (course == null || session == null) {
            Toast.makeText(this, "Course not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        User loggedIn = db.UserDao().getUserById(session.getUserID());
        if (loggedIn == null || !canAddHomework(loggedIn)) {
            Toast.makeText(this, "You cannot add homework for this course", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        creator.setText(getString(R.string.homework_creator_format, loggedIn.getName()));
        submit.setOnClickListener(view -> {
            String note = description.getText().toString().trim();
            String pdfLink = link.getText().toString().trim();
            description.setError(null);
            link.setError(null);
            if (note.isEmpty()) {
                description.setError("Please enter description");
                return;
            }
            if (pdfLink.isEmpty()) {
                link.setError("Link cannot be empty");
                return;
            }
            if (!isHttpLink(pdfLink)) {
                link.setError("Enter a valid http or https link");
                return;
            }
            Homework homework = new Homework(course.getId(), loggedIn.getId(), note, pdfLink);
            db.HomeworkDao().insert(homework);
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }

    private void findViews() {
        creator = findViewById(R.id.homework_creator);
        description = findViewById(R.id.homework_description);
        link = findViewById(R.id.homework_link);
        submit = findViewById(R.id.submit_homework);
    }

    private boolean canAddHomework(User loggedIn) {
        if (loggedIn.getUserType() == 0) {
            return course.getTeacherID() == loggedIn.getId();
        }
        if (loggedIn.getUserType() == 1) {
            TA ta = db.TADao().getRelation(course.getId(), loggedIn.getId());
            return ta != null && ta.isApproved();
        }
        return false;
    }

    private boolean isHttpLink(String pdfLink) {
        Uri uri = Uri.parse(pdfLink);
        String scheme = uri.getScheme();
        return uri.getHost() != null
                && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
    }
}
