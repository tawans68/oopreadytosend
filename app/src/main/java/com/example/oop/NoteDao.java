package com.example.oop;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(NoteEntity note);

    @Query("SELECT * FROM notes ORDER BY createdDate DESC")
    List<NoteEntity> getAll();

    @Delete
    void delete(NoteEntity note);
}
