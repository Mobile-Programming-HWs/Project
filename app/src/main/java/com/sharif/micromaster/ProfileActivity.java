package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    EditText name;
    TextView email;
    RecyclerView recyclerView;
    List<Course> courseList;
    SharedPreferences sharedPreferences;
    SharedPreferences loggedInPreferences;
    SharedPreferences.Editor editor;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = this.getSharedPreferences("usernames", Context.MODE_PRIVATE);
        loggedInPreferences = this.getSharedPreferences("logged", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String loggedInEmail = loggedInPreferences.getString("logged", "NULL");
        name = findViewById(R.id.profile_name);
        name.setText(sharedPreferences.getString(loggedInEmail, "Choose a name"));
        email = findViewById(R.id.profile_email);
        email.setText(loggedInEmail);
        recyclerView = findViewById(R.id.profile_courses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseList = new ArrayList<>();
        for (Course course: Course.courses) {
            if (course.lecturer.equals(loggedInEmail)) {
                courseList.add(course);
            }
        }
        adapter = new CustomAdapter(courseList, getApplicationContext());
        recyclerView.setAdapter(adapter);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editor.putString(loggedInEmail, (String) charSequence);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}