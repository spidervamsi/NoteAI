package com.example.noteai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    static ArrayList<String> text =new ArrayList<String>();
    static MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i=0;i<30;i++){
            text.add(Integer.toString(i));
        }
//    try{
//
//        SQLiteDatabase noteDB = this.openOrCreateDatabase("NoteAI",MODE_PRIVATE,null);
////
//        noteDB.execSQL("CREATE TABLE IF NOT EXISTS notes (body VARCHAR, id INTEGER PRIMARY KEY)");
//        noteDB.execSQL("INSERT INTO notes (body) VALUES ('heyyyy')");
//        noteDB.execSQL("INSERT INTO notes (body) VALUES ('bro')");
//        noteDB.execSQL("INSERT INTO notes (body) VALUES ('how are you')");
//
//        Cursor c = noteDB.rawQuery("SELECT * FROM notes",null);
//        int bodyIndex = c.getColumnIndex("body");
//        int idIndex = c.getColumnIndex("id");
////
//        c.moveToFirst();
//        do {
//            System.out.println("id:"+Integer.toString(c.getColumnIndex(Integer.toString(idIndex)))+" body:"+c.getColumnIndex(Integer.toString(bodyIndex)));
//
//        }while(c.moveToNext());
////
//    }catch (Exception e){
//        System.out.println("Main Exception "+e.toString());
//    }



        recyclerView = findViewById(R.id.recycle);
        myAdapter = new MyAdapter(this,text);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
}
