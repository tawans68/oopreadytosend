package com.example.oop;

import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckListNote extends Note {
    private List<String> items;

    public CheckListNote(String title) {
        super(title);
        this.items = new ArrayList<>();
        this.createdDate = new Date();
    }

    public CheckListNote(String title, Date createdDate, List<String> items) {
        super(title);
        this.items = items;
        this.createdDate = createdDate;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void addItem(String item) {
        this.items.add(item);
    }

    @Override
    public String getNoteType() {
        return "Checklist Note";
    }

    @Override
    public void displayContent() {
        Log.d("NoteApp", "Title: " + getTitle());
        Log.d("NoteApp", "Items:");
        for (String item : items) {
            Log.d("NoteApp", "- " + item);
        }
    }

    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(getTitle()).append("\nItems:\n");
        for (String item : items) {
            sb.append("- ").append(item).append("\n");
        }
        return sb.toString();
    }
}
