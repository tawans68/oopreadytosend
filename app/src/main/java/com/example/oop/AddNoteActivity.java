package com.example.oop;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private RadioGroup typeGroup;
    private RadioButton radioText;
    private Spinner userSpinner;
    private Button saveButton, addNewUserBtn, backBtn;

    private List<User> userList;
    private List<String> userNames;
    private ArrayAdapter<String> adapter;

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
        addNewUserBtn = findViewById(R.id.addNewUserBtn);
        backBtn = findViewById(R.id.backBtn);

        setupUserSpinner();

        saveButton.setOnClickListener(v -> saveNote());
        
        // เมื่อคลิกปุ่มบวก (+) จะแสดงหน้าต่างเพิ่ม User ใหม่
        addNewUserBtn.setOnClickListener(v -> showAddUserDialog());

        // ปุ่มย้อนกลับ
        backBtn.setOnClickListener(v -> finish());
    }

    private void setupUserSpinner() {
        userList = new ArrayList<>();
        userList.add(new StudentUser("tawan seankham", "tawan.s68@psru.ac.th", "6815247006"));
        userList.add(new StaffUser("Wanarat", "wanarat@email.com", "Computer Science"));

        userNames = new ArrayList<>();
        for (User u : userList) {
            userNames.add(u.getName() + " (" + u.getRole() + ")");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Custom User");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("User Name");
        layout.addView(nameInput);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Email Address");
        layout.addView(emailInput);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            
            if (!name.isEmpty() && !email.isEmpty()) {
                // สร้างวัตถุ User ใหม่ (ใช้ StudentUser เป็นฐานตามหลัก OOP)
                User newUser = new StudentUser(name, email, "Custom-ID");
                userList.add(newUser);
                userNames.add(newUser.getName() + " (" + newUser.getRole() + ")");
                adapter.notifyDataSetChanged(); // อัปเดต Spinner ทันที
                
                // เลือก User ที่เพิ่งเพิ่มให้เลย
                userSpinner.setSelection(userList.size() - 1);
                
                Toast.makeText(this, "User added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter name and email", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveNote() {
        String title = editTitle.getText().toString().trim();
        String contentStr = editContent.getText().toString().trim();
        int selectedUserIndex = userSpinner.getSelectedItemPosition();

        if (title.isEmpty() || contentStr.isEmpty() || selectedUserIndex < 0) {
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
                finish();
            });
        });
    }
}
