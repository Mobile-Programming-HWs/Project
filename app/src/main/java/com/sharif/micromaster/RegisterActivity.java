package com.sharif.micromaster;

import static com.sharif.micromaster.BitmapHelper.drawableToBitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    EditText name;
    EditText password;
    EditText email;
    RadioGroup userType;
    ImageView imageView;

    Button upload;
    Button register;
    Button take;
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
            if (type == -1) {
                Toast.makeText(RegisterActivity.this, "Choose an account type", Toast.LENGTH_SHORT).show();
                return;
            }
            User user = db.UserDao().getUser(email.getText().toString());
            if (user != null) {
                Toast.makeText(this, "This email is already registered!", Toast.LENGTH_SHORT).show();
            } else {
                user = new User(email.getText().toString(), password.getText().toString(), name.getText().toString(),
                        type, BitmapHelper.bitmapToString(bitmap));
                long userId = db.UserDao().insert(user);
                if (userId == -1) {
                    Toast.makeText(this, "This email is already registered!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        upload.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
        take.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null || data.getData() == null) {
                Toast.makeText(RegisterActivity.this, "image cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri imageData = data.getData();
            try {
                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                if (selectedBitmap == null) {
                    Toast.makeText(RegisterActivity.this, "image cannot be opened", Toast.LENGTH_SHORT).show();
                    return;
                }
                bitmap = selectedBitmap;
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(RegisterActivity.this, "image cannot be opened", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = null;
            if (data != null && data.getExtras() != null) {
                photo = (Bitmap) data.getExtras().get("data");
            }
            if (photo == null) {
                Toast.makeText(RegisterActivity.this, "image cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            bitmap = photo;
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
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
        take = findViewById(R.id.take_img);
    }


}
