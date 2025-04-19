package com.example.mobileapp;

public class HistoryItem {
    private String resultText;
    private String dateTime;

    public HistoryItem(String resultText, String dateTime) {
        this.resultText = resultText;
        this.dateTime = dateTime;
    }

    public String getResultText() {
        return resultText;
    }

    public String getDateTime() {
        return dateTime;
    }
}