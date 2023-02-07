package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText emailView;
    private EditText passwordView;
    private Button login;
    private Button register;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        db = Database.getInstance(this);
        login.setOnClickListener(view -> {
            boolean isValid = true;
            if (emailView.getText().toString().isEmpty()) {
                emailView.setError("Please enter email");
                isValid = false;
            }
            if (passwordView.getText().toString().isEmpty()) {
                passwordView.setError("Please enter password");
                isValid = false;
            }
            if (!isValid)
                return;
            login(emailView.getText().toString(), passwordView.getText().toString());

        });
        register.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String email, String password) {
        User user = db.UserDao().getUser(emailView.getText().toString());
        if (user == null) {
            Toast.makeText(MainActivity.this, "Entered email does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(user.getPassword())) {
            Toast.makeText(MainActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
        db.LoggedInUserDao().deleteAll();
        db.LoggedInUserDao().insert(new LoggedInUser(user.getId()));
        Intent intent = new Intent(this, CoursesListActivity.class);
        startActivity(intent);
    }


    private void findViews() {
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
    }
}