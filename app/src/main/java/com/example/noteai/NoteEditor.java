package com.example.noteai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class NoteEditor extends AppCompatActivity {

    TextView title,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        title = findViewById(R.id.title_editor);
        desc = findViewById(R.id.desc_editor);

        loadData();
    }

    private void loadData(){
        if(getIntent().hasExtra("Title")){
            title.setText(getIntent().getStringExtra("Title"));
        }
        if(getIntent().hasExtra("Desc")){
            desc.setText(getIntent().getStringExtra("Desc"));
        }
    }


}
