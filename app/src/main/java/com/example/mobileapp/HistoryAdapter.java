package com.example.mobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyList;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public HistoryAdapter(List<HistoryItem> historyList, OnDeleteClickListener listener) {
        this.historyList = historyList;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view, deleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = historyList.get(position);
        holder.resultText.setText(item.getResultText());
        holder.dateText.setText(item.getDateTime());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView resultText;
        TextView dateText;
        ImageButton deleteButton;

        public HistoryViewHolder(@NonNull View itemView, OnDeleteClickListener listener) {
            super(itemView);
            resultText = itemView.findViewById(R.id.resultText);
            dateText = itemView.findViewById(R.id.dateText);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }
}
