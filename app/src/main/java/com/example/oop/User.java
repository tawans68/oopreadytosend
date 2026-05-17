package com.example.oop;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private String name;
    private String email;
    private List<Note> myNotes;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.myNotes = new ArrayList<>();
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Note> getMyNotes() { return myNotes; }
    public void setMyNotes(List<Note> myNotes) { this.myNotes = myNotes; }


    public void addNote(Note note) {
        this.myNotes.add(note);
    }

    public abstract String getRole();
    public abstract void showInfo();


    public void displayAllNotes() {
        Log.d("NoteApp", "--- รายการโน้ตของ " + name + " ---");
        for (Note note : myNotes) {
            note.displayContent();
        }
    }
}

class StudentUser extends User {
    private String studentId;

    public StudentUser(String name, String email, String studentId) {
        super(name, email);
        this.studentId = studentId;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    @Override
    public String getRole() { return "Student"; }

    @Override
    public void showInfo() {
        Log.d("NoteApp", "User: " + getName() + " | ID: " + studentId + " [" + getRole() + "]");
    }
}

class StaffUser extends User {
    private String department;

    public StaffUser(String name, String email, String department) {
        super(name, email);
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String getRole() { return "Staff"; }

    @Override
    public void showInfo() {
        Log.d("NoteApp", "User: " + getName() + " | Dept: " + department + " [" + getRole() + "]");
    }
}
