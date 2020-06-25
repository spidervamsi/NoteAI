package com.example.noteai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    try{

        SQLiteDatabase noteDB = this.openOrCreateDatabase("NoteAI",MODE_PRIVATE,null);
//

        try{noteDB.execSQL("DROP TABLE users");
        }catch (Exception e){
            Log.i("notedev exception",e.getMessage());
        }

        noteDB.execSQL("CREATE TABLE IF NOT EXISTS users (person_id INTEGER PRIMARY KEY, body TEXT, age INT(3))");
        noteDB.execSQL("INSERT INTO users (body,age) VALUES ('type1',21)");
        noteDB.execSQL("INSERT INTO users (body,age) VALUES ('type4',22)");
        noteDB.execSQL("INSERT INTO users (body,age) VALUES ('type3',24)");

        Cursor c = noteDB.rawQuery("SELECT * FROM users",null);
        int bodyIndex = c.getColumnIndex("body");
        int ageIndex = c.getColumnIndex("age");
        int idIndex = c.getColumnIndex("person_id");
//
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            Log.i("notedev","body:"+c.getString(bodyIndex));
            Log.i("notedev","age:"+Integer.toString(c.getInt(ageIndex)));
            Log.i("notedev","id:"+Integer.toString(c.getInt(idIndex)));
            c.moveToNext();
        }


        noteDB.execSQL("DELETE FROM users WHERE person_id=1");

        c = noteDB.rawQuery("SELECT * FROM users",null);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            Log.i("notedev","body:"+c.getString(bodyIndex));
            Log.i("notedev","age:"+Integer.toString(c.getInt(ageIndex)));
            Log.i("notedev","id:"+Integer.toString(c.getInt(idIndex)));
            c.moveToNext();
        }

        noteDB.execSQL("INSERT INTO users (body,age) VALUES ('type5',26)");
        c = noteDB.rawQuery("SELECT * FROM users",null);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            Log.i("notedev","body:"+c.getString(bodyIndex));
            Log.i("notedev","age:"+Integer.toString(c.getInt(ageIndex)));
            Log.i("notedev","id:"+Integer.toString(c.getInt(idIndex)));
            c.moveToNext();
        }



    }catch (Exception e){
        System.out.println("notedev exception"+e.toString());
    }



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
