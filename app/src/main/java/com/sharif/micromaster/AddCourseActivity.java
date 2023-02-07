package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCourseActivity extends AppCompatActivity {

    EditText name;
    EditText units;
    EditText description;
    Button submit;
    Button cancel;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        findViews();
        sharedPreferences = this.getSharedPreferences("logged", Context.MODE_PRIVATE);
        String loggedIn = sharedPreferences.getString("logged", "DEFAULT");
        submit.setOnClickListener(view -> {
            if (name.getText().toString().isEmpty() || units.getText().toString().isEmpty()) {
                return;
            }
            Course.courses.add(new Course(R.drawable.ic_launcher_foreground, units.getText().toString(),
                    name.getText().toString(), description.getText().toString(), loggedIn));
            finish();
        });
        cancel.setOnClickListener(view -> finish());
    }

    private void findViews() {
        name = findViewById(R.id.addCourseName);
        units = findViewById(R.id.addCourseUnits);
        description = findViewById(R.id.addCourseDescription);
        submit = findViewById(R.id.submit_course);
        cancel = findViewById(R.id.cancel_submission);
    }
}