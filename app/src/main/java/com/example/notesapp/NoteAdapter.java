package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;

    // Modern color palette
    private final int[] noteColors = {
            Color.parseColor("#E3F2FD"), // Light blue
            Color.parseColor("#E8F5E9"), // Light green
            Color.parseColor("#FFF8E1"), // Light amber
            Color.parseColor("#FCE4EC"), // Light pink
            Color.parseColor("#F3E5F5"), // Light purple
            Color.parseColor("#E0F7FA"), // Light cyan
            Color.parseColor("#E8EAF6"), // Light indigo
            Color.parseColor("#FFFDE7")  // Light yellow
    };

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextview.setText(Utility.timestampToString(note.timestamp));

        // Set note color
        int color;
        if (note.getColor() != 0) {
            // Use the color from Firestore
            color = note.getColor();
        } else {
            // Assign a color from our palette based on position
            color = noteColors[position % noteColors.length];
        }

        // Apply color to card
        holder.noteCard.setCardBackgroundColor(color);

        // Adjust text color based on background brightness
        int textColor = isColorDark(color) ? Color.WHITE : Color.BLACK;
        holder.titleTextView.setTextColor(textColor);
        holder.contentTextView.setTextColor(textColor);
        holder.timestampTextview.setTextColor(textColor);

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, NoteDetailScreen.class);
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);
            intent.putExtra("color", color);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView, timestampTextview;
        CardView noteCard;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title);
            contentTextView = itemView.findViewById(R.id.note_content);
            timestampTextview = itemView.findViewById(R.id.note_timestamp_textView);
            noteCard = itemView.findViewById(R.id.note_card);
        }
    }

    // Helper method to determine if a color is dark
    private boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}