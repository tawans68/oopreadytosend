package com.example.oop;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public abstract class Note {
    public Date createdDate;
    
    @SerializedName("title")
    private String title;

    public Note() {
        this.createdDate = new Date();
    }

    public Note(String title) {
        this();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract String getNoteType();
    public abstract void displayContent();
    public abstract String display();
}
