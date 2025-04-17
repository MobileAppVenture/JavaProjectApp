package com.example.mobileapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.task.vision.detector.Detection;

import java.io.IOException;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView resultView;
    private Button historyButton;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        imageView = findViewById(R.id.instructionNew);
        resultView = findViewById(R.id.resultText);
        historyButton = findViewById(R.id.nextButton4);
        returnButton = findViewById(R.id.returnButton);

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString == null) {
            Toast.makeText(this, "Ошибка: изображение не передано", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri imageUri = Uri.parse(imageUriString);
        imageView.setImageURI(imageUri);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            ObjectDetectorHelper detectorHelper = new ObjectDetectorHelper(this);
            List<Detection> results = detectorHelper.detect(bitmap);

            if (results != null && !results.isEmpty()) {
                StringBuilder resultText = new StringBuilder();
                for (Detection detection : results) {
                    if (!detection.getCategories().isEmpty()) {
                        String label = detection.getCategories().get(0).getLabel();
                        float score = detection.getCategories().get(0).getScore();
                        resultText.append(label)
                                .append(" (")
                                .append(String.format("%.1f", score * 100))
                                .append("%)").append("\n");
                    }
                }
                resultView.setText(resultText.toString());
            } else {
                resultView.setText("Объекты не распознаны");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка обработки изображения", Toast.LENGTH_SHORT).show();
        }

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(AnalysisActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        returnButton.setOnClickListener(v -> finish());
    }
}
