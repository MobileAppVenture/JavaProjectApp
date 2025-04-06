package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button again = findViewById(R.id.again);
        Button returnButton = findViewById(R.id.returnButton);
        again.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, NextActivity.class);
            startActivity(intent);
        });
        returnButton.setOnClickListener(v -> {
            finish();
        });

    }
}
