package com.example.mobileapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AnalysisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        ImageView imageView = findViewById(R.id.instructionNew);
        Button historyButton = findViewById(R.id.nextButton4);
        Button returnButton = findViewById(R.id.returnButton);

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(
                    this,
                    "Ошибка: изображение не передано",
                    Toast.LENGTH_SHORT
            ).show();
        }
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    AnalysisActivity.this,
                    HistoryActivity.class
            );
            startActivity(intent);
        });
        returnButton.setOnClickListener(v -> {
            finish();
        });

    }
}