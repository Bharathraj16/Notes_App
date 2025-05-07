package com.example.notesapp;

import com.google.firebase.Timestamp;

public class Note {
    String title;
    String content;
    Timestamp timestamp;
    int color; // Add color field

    public Note() {
        // Required empty constructor for Firestore
    }

    // Updated constructor with color
    public Note(String title, String content, Timestamp timestamp, int color) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.color = color;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}