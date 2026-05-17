package com.example.oop;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notes")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String title;

    @NonNull
    public String type; // "text" หรือ "checklist"

    public String checklistItemsJson; // null ถ้าเป็น textnote
    public String content; // null ถ้าเป็น checklist

    public Date createdDate;

    // เพิ่มข้อมูล User
    public String ownerName;
    public String ownerEmail;

    public NoteEntity(@NonNull String title, @NonNull String type, String checklistItemsJson, String content, Date createdDate, String ownerName, String ownerEmail) {
        this.title = title;
        this.type = type;
        this.checklistItemsJson = checklistItemsJson;
        this.content = content;
        this.createdDate = createdDate;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
    }
}
