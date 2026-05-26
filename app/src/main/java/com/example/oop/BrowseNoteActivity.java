package com.example.oop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BrowseNoteActivity extends AppCompatActivity {
    TextView showNote, showNoteFromAPI;

    // Search UI Components (Requirement 3)
    EditText searchEditText;
    Button searchBtn;
    ProgressBar searchProgressBar;
    TextView searchResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showNote = findViewById(R.id.textView3);
        showNoteFromAPI = findViewById(R.id.textView5);

        // Bind Search UI (Requirement 3)
        searchEditText = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchBtn);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        searchResultTextView = findViewById(R.id.searchResultTextView);

        // Search Button Event (Requirement 3)
        searchBtn.setOnClickListener(v -> {
            performSearch();
        });

        // load data from db
        Executors.newSingleThreadExecutor().execute(() -> {
            List<NoteEntity> entities = AppDatabase.getInstance(this).noteDao().getAll();
            List<Note> notes = new ArrayList<>();
            for (NoteEntity e : entities) {
                notes.add(NoteMapper.fromEntity(e));
            }

            // display on UI thread
            runOnUiThread(() -> {
                StringBuilder sb = new StringBuilder();
                for (Note n : notes) {
                    if (n != null) {
                        sb.append(n.display()).append("\n------------------\n");
                    }
                }
                showNote.setText(sb.toString());
            });
        });

        // load from API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<TextNote>> call = apiService.getTextNote();

        call.enqueue(new Callback<List<TextNote>>() {
            @Override
            public void onResponse(Call<List<TextNote>> call, Response<List<TextNote>> response) {
                if (!response.isSuccessful()) {
                    showNoteFromAPI.setText("Error Code: " + response.code());
                    return;
                }

                List<TextNote> notes = response.body();
                StringBuilder builder = new StringBuilder();
                if (notes != null) {
                    for (TextNote n : notes) {
                        builder.append("Title: ").append(n.getTitle()).append("\n")
                                .append("Body: ").append(n.getTextContent()).append("\n\n");
                    }
                }
                showNoteFromAPI.setText(builder.toString());
            }

            @Override
            public void onFailure(Call<List<TextNote>> call, Throwable t) {
                showNoteFromAPI.setText("Failed: " + t.getMessage());
            }
        });
    }

    // Perform Search Logic (Requirement 3)
    private void performSearch() {
        searchResultTextView.setText(""); // ล้างข้อความเก่า
        searchProgressBar.setVisibility(View.VISIBLE); // แสดง ProgressBar

        // ใช้ Thread ในการโหลดข้อมูล (สมมติ)
        new Thread(() -> {
            try {
                // ดีเลย์ 2 วินาที
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // กลับมาทำงานบน UI Thread
            runOnUiThread(() -> {
                searchProgressBar.setVisibility(View.GONE); // ซ่อน ProgressBar
                searchResultTextView.setText("ไม่พบข้อมูล"); // แสดงข้อความ "ไม่พบข้อมูล"
            });
        }).start();
    }
}
