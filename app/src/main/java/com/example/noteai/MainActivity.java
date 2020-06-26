package com.example.noteai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
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
        Log.i("notedev","oncreate");

        setContentView(R.layout.activity_main);

        for(int i=0;i<30;i++){
            text.add(Integer.toString(i));
        }
    try{

        Model model = new Model(this,"NoteAI",null,1);
        SQLiteDatabase noteDB = model.getWritableDatabase();
        long newRowId;
        for(String t:text){
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_BODY, t);
            newRowId = noteDB.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
            Log.i("notedev","id "+Long.toString(newRowId));
        }


        // To read from Database
        String[] projection = {
                "id",
                FeedReaderContract.FeedEntry.COLUMN_BODY
        };

//        String selection = FeedReaderContract.FeedEntry.COLUMN_BODY + " = ?";
        String selection = projection[0] +  " = ?";
        String[] selectionArgs = { "6" };


        Cursor cursor = noteDB.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while(cursor.moveToNext()) {
            String item = cursor.getString(1);
            Log.i("notedev","itemId : "+item);
        }
        cursor.close();





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
