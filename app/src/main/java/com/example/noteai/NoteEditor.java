package com.example.noteai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;


public class NoteEditor extends AppCompatActivity {

    EditText text;
    int pos;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        text = findViewById(R.id.completeText);
        loadData();
    }

    private void loadData(){
        if(getIntent().hasExtra("Text")){
            text.setText(getIntent().getStringExtra("Text"));
        }
        if(getIntent().hasExtra("Pos")){
            pos = getIntent().getIntExtra("Pos",-1);
        }

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.text.set(pos,String.valueOf(charSequence));
                MainActivity.myAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}
