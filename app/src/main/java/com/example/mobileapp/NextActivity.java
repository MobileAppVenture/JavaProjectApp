package com.example.mobileapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class NextActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;
    private Uri selectedImageUri;
    private Uri photoUri;
    private ImageView instructionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Log.d("NextActivity", "onCreate called");

        instructionImageView = findViewById(R.id.instruction);
        ImageButton galleryButton = findViewById(R.id.gallery);
        ImageButton photoButton = findViewById(R.id.photo);
        Button nextButton = findViewById(R.id.nextButton2);

        galleryButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        });

        photoButton.setOnClickListener(v -> {
            Log.d("NextActivity", "Photo button clicked");
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE);
            } else {
                openCamera();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (selectedImageUri == null && photoUri == null) {
                Log.d("NextActivity", "No image selected");
                Toast.makeText(
                        NextActivity.this,
                        "Error: Photo not selected",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Intent intent = new Intent(NextActivity.this, ViewImage.class);
                if (selectedImageUri != null) {
                    intent.putExtra("imageUri", selectedImageUri.toString());
                } else if (photoUri != null) {
                    intent.putExtra("imageUri", photoUri.toString());
                }
                startActivity(intent);
            }
        });
    }

    private void openCamera() {
        try {
            File photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.mobileapp.fileprovider",
                    photoFile
            );
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String fileName = "photo_" + System.currentTimeMillis();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Log.d("NextActivity", "Camera permission denied");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (photoUri != null) {
                    instructionImageView.setImageURI(photoUri);
                    selectedImageUri = null;
                }
            } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                selectedImageUri = data.getData();
                instructionImageView.setImageURI(selectedImageUri);
                photoUri = null;
            }
        }
    }
}
