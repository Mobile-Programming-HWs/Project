package com.sharif.micromaster;

import static com.sharif.micromaster.BitmapHelper.drawableToBitmap;
import static com.sharif.micromaster.BitmapHelper.getBytesFromBitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    EditText name;
    EditText password;
    EditText email;
    RadioGroup userType;
    ImageView imageView;

    Button upload;
    Button register;
    Database db;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        Drawable d = getResources().getDrawable(R.drawable.ic_launcher_foreground);
        bitmap = drawableToBitmap(d);
        imageView.setImageBitmap(bitmap);
        db = Database.getInstance(this);
        register.setOnClickListener(view -> {
            if (email.getText().toString().isEmpty()) {
                Toast.makeText(RegisterActivity.this, "email cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.getText().toString().isEmpty()) {
                Toast.makeText(RegisterActivity.this, "password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            int type = userType.indexOfChild(findViewById(userType.getCheckedRadioButtonId()));
            User user = db.UserDao().getUser(email.getText().toString());
            if (user != null) {
                Toast.makeText(this, "This email is already registered!", Toast.LENGTH_SHORT).show();
            } else {
                user = new User(email.getText().toString(), password.getText().toString(), name.getText().toString(),
                        type, getBytesFromBitmap(bitmap));
                db.UserDao().insert(user);
                Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
        upload.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(RegisterActivity.this, "image cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri imageData = data.getData();
            imageView.setImageURI(imageData);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void findViews() {
        name = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        email = findViewById(R.id.editTextTextEmailAddress);
        userType = findViewById(R.id.userType);
        imageView = findViewById(R.id.profile_image_upload);
        upload = findViewById(R.id.upload_img);
        register = findViewById(R.id.register_button);
    }


}