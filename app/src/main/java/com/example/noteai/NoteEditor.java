package com.example.noteai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;


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
                    model.updateNote(rowId, String.valueOf(charSequence));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Log.i("notedev","after text change");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("notedev","destroyed");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.del:
                Log.i("notedev","delete option item selected "+Long.toString(rowId));
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Delete the Note")
                            .setMessage("Are you sure you want to delete the note?")
                            .setIcon(R.drawable.ic_baseline_delete_note);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            model.deleteNote(rowId);
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    builder.show();
                }
                catch (Exception e){
                    Log.i("notedev",e.getMessage());
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
