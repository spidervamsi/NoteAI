package com.example.noteai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    static MyAdapter myAdapter;
    static ArrayList<RowData> text =new ArrayList<RowData>();
    Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("notedev","oncreate");

        setContentView(R.layout.activity_main);

        populate();
        fab();

    }

    private void fab(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(),NoteEditor.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("notedev","on restart");
        populate();
    }

    private void populate(){
        text.clear();
        try{
            model = new Model(getApplicationContext(),"NoteAI",null,1);
            Cursor cursor = model.fetchAll();
            while(cursor.moveToNext()) {
                RowData row = new RowData(cursor.getLong(0),cursor.getString(1));
                text.add(row);
            }
            cursor.close();
        }catch (Exception e){
            System.out.println("notedev exception"+e.toString());
        }
        recyclerView = findViewById(R.id.recycle);
        myAdapter = new MyAdapter(this,text);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                Log.i("notedev","Main Activity del button "+myAdapter.getSelectedValues());

                for(Long i:myAdapter.getSelectedValues()){
                    model.deleteNote(i);
                }
               recreate();

        }
        return super.onOptionsItemSelected(item);
    }


}
