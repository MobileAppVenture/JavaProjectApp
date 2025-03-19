package com.example.mobileapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Uri imageUri = getIntent().getParcelableExtra("imageUri");
        ImageView imageView = findViewById(R.id.fullScreenImageView);
        imageView.setImageURI(imageUri);
    }
}