package com.example.mobileapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class ViewImage extends AppCompatActivity {

    private Uri imageUri;
    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = findViewById(R.id.instruction2);
        Button nextButton = findViewById(R.id.nextButton4);
        Button returnButton = findViewById(R.id.nextButton3);

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            displayImage(imageUri);
        } else {
            Log.e("ViewImage", "imageUri is null");
            Toast.makeText(this, "Ошибка: изображение не найдено", Toast.LENGTH_SHORT).show();
        }

        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewImage.this, AnalysisActivity.class);
            if (imageUri != null) {
                intent.putExtra("imageUri", imageUri.toString());
            }
            startActivity(intent);
        });
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewImage.this, NextActivity.class);
            startActivity(intent);
            finish();
        });


    }

    private void displayImage(Uri uri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e("ViewImage", "Ошибка загрузки изображения", e);
            Toast.makeText(this, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show();
        }
    }

    private void processImage() {
        // Здесь можно добавить обработку изображения
        Toast.makeText(this, "Обработка изображения...", Toast.LENGTH_SHORT).show();


    }
}
