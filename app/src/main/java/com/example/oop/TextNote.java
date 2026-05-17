package com.example.oop;

import android.util.Log;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class TextNote extends Note {
    @SerializedName("body")
    private String content;

    public TextNote() {
        super();
    }

    public TextNote(String title, String content) {
        super(title);
        this.content = content;
        this.createdDate = new Date();
    }

    public TextNote(String title, Date createdDate, String content) {
        super(title);
        this.content = content;
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public String getTextContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getNoteType() {
        return "Text Note";
    }

    @Override
    public void displayContent() {
        Log.d("NoteApp", "Title: " + getTitle());
        Log.d("NoteApp", "Content: " + content);
    }

    @Override
    public String display() {
        return "Title: " + getTitle() + "\nContent: " + content;
    }
}
