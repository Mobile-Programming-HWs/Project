package com.sharif.micromaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;
    SharedPreferences sharedPreferences;
    SharedPreferences loggedInPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor loggedInEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        sharedPreferences = this.getSharedPreferences("users", Context.MODE_PRIVATE);
        loggedInPreferences = this.getSharedPreferences("logged", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loggedInEditor = loggedInPreferences.edit();
        login.setOnClickListener(view -> {
            boolean isValid = true;
            if (email.getText().toString().isEmpty()) {
                email.setError("Please enter email");
                isValid = false;
            }
            if (password.getText().toString().isEmpty()) {
                password.setError("Please enter password");
                isValid = false;
            }
            if (!isValid)
                return;
            login(email.getText().toString(), password.getText().toString());

        });
        register.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String email, String password) {
        if (!sharedPreferences.contains(email)) {
            Toast.makeText(MainActivity.this, "Entered email does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(sharedPreferences.getString("users", email))) {
            Toast.makeText(MainActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
        loggedInEditor.putString("logged", email);
        Intent intent = new Intent(this, CoursesListActivity.class);
        startActivity(intent);
    }


    private void findViews() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
    }
}