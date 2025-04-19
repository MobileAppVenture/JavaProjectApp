package com.example.mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.tensorflow.lite.task.vision.detector.Detection;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            Toast.makeText(this, "Error: image not provided", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri imageUri = Uri.parse(imageUriString);
        imageView.setImageURI(imageUri);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            ObjectDetectorHelper detectorHelper = new ObjectDetectorHelper(this);
            List<Detection> results = detectorHelper.detect(bitmap);

            String resultString;
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
                resultString = resultText.toString();
                resultView.setText(resultString);
            } else {
                resultString = "Objects are not recognized";
                resultView.setText(resultString);
            }

            saveResultToHistory(resultString);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Image processing error", Toast.LENGTH_SHORT).show();
        }

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(AnalysisActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(AnalysisActivity.this, NextActivity.class);
            startActivity(intent);
        });
    }

    private void saveResultToHistory(String result) {
        SharedPreferences prefs = getSharedPreferences("history_prefs", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = prefs.getString("history_list", null);
        Type type = new TypeToken<ArrayList<HistoryItem>>(){}.getType();
        ArrayList<HistoryItem> historyList = gson.fromJson(json, type);
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 3);  // добавляем 3 часа

        String dateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(calendar.getTime());
        historyList.add(0, new HistoryItem(result, dateTime));

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("history_list", gson.toJson(historyList));
        editor.apply();
    }
}
