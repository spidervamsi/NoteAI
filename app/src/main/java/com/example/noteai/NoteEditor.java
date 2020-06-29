package com.example.noteai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;


public class NoteEditor extends AppCompatActivity {

    Model model;

    EditText text;
    long rowId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        text = findViewById(R.id.completeText);
        text.setSelection(text.getText().length());


        model = new Model(getApplicationContext(),"NoteAI",null,1);
        loadData();
    }

    private void loadData(){
        if(getIntent().hasExtra("Text")){
            text.setText(getIntent().getStringExtra("Text"));
            text.setSelection(text.getText().length());
        }

        rowId = getIntent().getLongExtra("RowId",-1);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.i("notedev","before text change");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("notedev","on Text change");
                if(rowId == -1){
                    rowId = model.insertBody(String.valueOf(charSequence));
                }else{
                    model.update(rowId, String.valueOf(charSequence));
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Log.i("notedev","after text change");
            }
        });
    }


}
