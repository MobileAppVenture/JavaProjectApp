package com.example.mobileapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements
        HistoryAdapter.OnDeleteClickListener {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<HistoryItem> historyList;

    private Button again;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        again = findViewById(R.id.again);
        returnButton = findViewById(R.id.returnButton);

        again.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, NextActivity.class);
            startActivity(intent);
        });

        returnButton.setOnClickListener(v -> finish());

        historyList = loadHistory();

        adapter = new HistoryAdapter(historyList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<HistoryItem> loadHistory() {
        SharedPreferences prefs = getSharedPreferences("history_prefs", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = prefs.getString("history_list", null);
        Type type = new TypeToken<ArrayList<HistoryItem>>() {
        }.getType();
        ArrayList<HistoryItem> list = gson.fromJson(json, type);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    private void saveHistory() {
        SharedPreferences prefs = getSharedPreferences(
                "history_prefs",
                MODE_PRIVATE
        );
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        editor.putString("history_list", gson.toJson(historyList));
        editor.apply();
    }

    @Override
    public void onDeleteClick(int position) {
        historyList.remove(position);
        saveHistory();
        adapter.notifyItemRemoved(position);
        Toast.makeText(
                this,
                "The element has been deleted",
                Toast.LENGTH_SHORT
        ).show();
    }
}
