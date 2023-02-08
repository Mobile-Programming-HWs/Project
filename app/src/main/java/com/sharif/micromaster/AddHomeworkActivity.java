package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        User loggedIn = db.UserDao().getUserById(db.LoggedInUserDao().user().getUserID());
        creator.setText("Creator: " + loggedIn.getName());
        Intent intent = getIntent();
        int courseId = intent.getIntExtra("course", 1);
        course = db.CourseDao().getCourse(courseId);
        submit.setOnClickListener(view -> {
            if (creator.getText().toString().isEmpty()) {
                Toast.makeText(this, "Creator cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (link.getText().toString().isEmpty()) {
                Toast.makeText(this, "Link cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Homework homework = new Homework(courseId, loggedIn.getId(), description.getText().toString(),
                    link.getText().toString());
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
}