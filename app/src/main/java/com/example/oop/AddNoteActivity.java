package com.example.oop;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private RadioGroup typeGroup;
    private RadioButton radioText;
    private Spinner userSpinner;
    private Button saveButton;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        typeGroup = findViewById(R.id.typeGroup);
        radioText = findViewById(R.id.radioText);
        userSpinner = findViewById(R.id.userSpinner);
        saveButton = findViewById(R.id.saveButton);

        setupUserSpinner();

        saveButton.setOnClickListener(v -> saveNote());
    }

    private void setupUserSpinner() {
        userList = new ArrayList<>();
        userList.add(new StudentUser("Tawan Seankham", "tawan.s68@psru.ac.th", "6815247006"));
        userList.add(new StaffUser("Wanarat", "wanarat@email.com", "Computer Science"));

        List<String> userNames = new ArrayList<>();
        for (User u : userList) {
            userNames.add(u.getName() + " (" + u.getRole() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);
    }

    private void saveNote() {
        String title = editTitle.getText().toString().trim();
        String contentStr = editContent.getText().toString().trim();
        int selectedUserIndex = userSpinner.getSelectedItemPosition();

        if (title.isEmpty() || contentStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User selectedUser = userList.get(selectedUserIndex);
        Note note;

        if (radioText.isChecked()) {
            note = new TextNote(title, contentStr);
        } else {
            CheckListNote clNote = new CheckListNote(title);
            String[] items = contentStr.split(",");
            for (String item : items) {
                clNote.addItem(item.trim());
            }
            note = clNote;
        }

        NoteEntity entity = NoteMapper.toEntity(note, selectedUser);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance(this).noteDao().insert(entity);
            runOnUiThread(() -> {
                Toast.makeText(this, "Note Saved by " + selectedUser.getName(), Toast.LENGTH_SHORT).show();
                finish(); // Close activity
            });
        });
    }
}
