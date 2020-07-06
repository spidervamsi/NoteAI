package com.example.noteai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    static MyAdapter myAdapter;
    static ArrayList<RowData> text =new ArrayList<RowData>();
    Model model;
    MenuItem mainDeleteButton;
    int REQUEST_PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("notedev","oncreate");

        setContentView(R.layout.activity_main);

        populate();
        fab();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},REQUEST_PERMISSION_CODE);
        int audio_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int ext_storage = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        if(mainDeleteButton!=null){
            mainDeleteButton.setVisible(false);
            myAdapter.setMenuItem(mainDeleteButton);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.main_menu, menu);

        try{
           MenuItem m = (MenuItem) menu.getItem(0);
            mainDeleteButton = m;
           m.setVisible(false);
           myAdapter.setMenuItem(mainDeleteButton);
        }
        catch (Exception e){
            Log.i("notedev","main del "+e.getMessage());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainMenuDel:
                Log.i("notedev","Main Activity del button "+myAdapter.getSelectedValues());
                if(myAdapter.getSelectedValues()!=null){
                    for(Long i:myAdapter.getSelectedValues()){
                        model.deleteNote(i);
                    }
                    recreate();
                }
        }
        return super.onOptionsItemSelected(item);
    }

}
