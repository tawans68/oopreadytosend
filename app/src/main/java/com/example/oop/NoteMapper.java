package com.example.oop;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class NoteMapper {
    static Gson gson = new Gson();

    // OOP -> Entity (รับ User เพิ่มเข้ามา)
    public static NoteEntity toEntity(Note note, User owner) {
        String ownerName = (owner != null) ? owner.getName() : "Unknown";
        String ownerEmail = (owner != null) ? owner.getEmail() : "Unknown";

        if (note instanceof TextNote) {
            return new NoteEntity(note.getTitle(), "text", null, ((TextNote) note).getContent(), note.createdDate, ownerName, ownerEmail);
        } else if (note instanceof CheckListNote) {
            String jsonItems = gson.toJson(((CheckListNote) note).getItems());
            return new NoteEntity(note.getTitle(), "checklist", jsonItems, null, note.createdDate, ownerName, ownerEmail);
        }
        return null;
    }

    // Entity -> OOP
    public static Note fromEntity(NoteEntity entity) {
        Note note = null;
        if (entity.type.equals("text")) {
            note = new TextNote(entity.title, entity.createdDate, entity.content);
        } else if (entity.type.equals("checklist")) {
            List<String> items = gson.fromJson(entity.checklistItemsJson, new TypeToken<List<String>>() {}.getType());
            note = new CheckListNote(entity.title, entity.createdDate, items);
        }
        return note;
    }
}
