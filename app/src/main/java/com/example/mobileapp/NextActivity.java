package com.example.mobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class NextActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> galleryLauncher;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ImageView imageView = findViewById(R.id.instruction);
                        imageView.setImageURI(selectedImageUri);
                    }
                }
        );

        ImageButton galleryButton = findViewById(R.id.gallery);
        galleryButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                galleryLauncher.launch(intent);
            }
        });

        ImageButton photoButton = findViewById(R.id.photo);
        photoButton.setOnClickListener(v -> {
        });

        Button nextButton = findViewById(R.id.nextButton2);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(NextActivity.this, ViewImage.class);
            intent.putExtra("imageUri", selectedImageUri);
            startActivity(intent);
        });
    }
}
