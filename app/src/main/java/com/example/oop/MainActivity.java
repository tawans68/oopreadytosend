package com.example.oop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button addBtn, browseBtn, fetchApiBtn;
    ProgressBar loadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addBtn);
        browseBtn = findViewById(R.id.browseBtn);
        fetchApiBtn = findViewById(R.id.fetchApiBtn);
        loadData = findViewById(R.id.progressBar);

        loadData.setVisibility(View.GONE);

        // API Event: Fetch notes from API
        fetchApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchNotesFromApi();
            }
        });

        // Event-driven: ไปหน้า AddNoteActivity
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // Event-driven: ไปหน้า BrowseNoteActivity
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> {
                        loadData.setVisibility(View.GONE);
                        Intent intent = new Intent(MainActivity.this, BrowseNoteActivity.class);
                        startActivity(intent);
                    });
                }).start();
            }
        });
    }

    private void fetchNotesFromApi() {
        loadData.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getTextNote().enqueue(new Callback<List<TextNote>>() {
            @Override
            public void onResponse(Call<List<TextNote>> call, Response<List<TextNote>> response) {
                loadData.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<TextNote> apiNotes = response.body();
                    saveApiNotesToDatabase(apiNotes);
                } else {
                    Toast.makeText(MainActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TextNote>> call, Throwable t) {
                loadData.setVisibility(View.GONE);
                Log.e("API", "Failure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch from API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveApiNotesToDatabase(List<TextNote> notes) {
        // จำกัดจำนวนที่จะเซฟแค่ 5 อันแรก เพื่อไม่ให้เยอะเกินไป
        int count = Math.min(notes.size(), 5);
        User apiUser = new StaffUser("API System", "api@system.com", "External API");

        Executors.newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < count; i++) {
                NoteEntity entity = NoteMapper.toEntity(notes.get(i), apiUser);
                AppDatabase.getInstance(this).noteDao().insert(entity);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Fetched and saved " + count + " notes from API", Toast.LENGTH_LONG).show();
            });
        });
    }
}
