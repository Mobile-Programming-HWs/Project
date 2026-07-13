package com.sharif.micromaster;

import static com.sharif.micromaster.BitmapHelper.drawableToBitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCourseActivity extends AppCompatActivity {

    EditText name;
    EditText units;
    EditText description;
    Button submit;
    Button cancel;
    Database db;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        db = Database.getInstance(this);
        findViews();
        submit.setOnClickListener(view -> {
            String courseName = name.getText().toString().trim();
            String courseDescription = description.getText().toString().trim();
            String unitText = units.getText().toString().trim();
            if (courseName.isEmpty()) {
                name.setError("Please enter course name");
                return;
            }
            if (courseDescription.isEmpty()) {
                description.setError("Please enter description");
                return;
            }
            int unitCount;
            try {
                unitCount = Integer.parseInt(unitText);
            } catch (NumberFormatException e) {
                units.setError("Please enter a valid number");
                return;
            }
            if (unitCount < 1 || unitCount > 6) {
                units.setError("Units must be between 1 and 6");
                return;
            }
            LoggedInUser session = db.LoggedInUserDao().user();
            if (session == null) {
                Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            User loggedIn = db.UserDao().getUserById(session.getUserID());
            if (loggedIn == null) {
                db.LoggedInUserDao().deleteAll();
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            Drawable d = getResources().getDrawable(R.drawable.ic_launcher_foreground);
            bitmap = drawableToBitmap(d);
            Course course = new Course(loggedIn.getId(), courseName, courseDescription,
                    unitCount, BitmapHelper.bitmapToString(bitmap));
            db.CourseDao().insert(course);
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
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
