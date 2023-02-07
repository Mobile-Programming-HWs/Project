package com.sharif.micromaster;

import static com.sharif.micromaster.BitmapHelper.drawableToBitmap;
import static com.sharif.micromaster.BitmapHelper.getBytesFromBitmap;

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
            if (name.getText().toString().isEmpty() || units.getText().toString().isEmpty()) {
                return;
            }
            User loggedIn = db.UserDao().getUserById(db.LoggedInUserDao().user().getUserID());
            Drawable d = getResources().getDrawable(R.drawable.ic_launcher_foreground);
            bitmap = drawableToBitmap(d);
            Course course = new Course(loggedIn.getId(), name.getText().toString(), description.getText().toString(),
                    Integer.parseInt(units.getText().toString()), getBytesFromBitmap(bitmap));
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