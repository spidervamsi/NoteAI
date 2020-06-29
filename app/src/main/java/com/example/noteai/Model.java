package com.example.noteai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Model extends SQLiteOpenHelper {

    public Model(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.i("notedev","dbcreate");
        sqLiteDatabase.execSQL(
                "create table "+FeedReaderContract.FeedEntry.TABLE_NAME +
                        " (id integer primary key, "+FeedReaderContract.FeedEntry.COLUMN_BODY+" text)"
        );
    }

    // To read from Database
    public Cursor fetchAll(){
        String[] projection = {
                "id",
                FeedReaderContract.FeedEntry.COLUMN_BODY
        };
        Cursor cursor = this.getReadableDatabase().query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        return cursor;
    }


    public long insertBody(String text){
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_BODY, text);
        long newRowId = this.getWritableDatabase().insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        return newRowId;
    }


    public void update(long id, String text) {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_BODY, text);

        this.getWritableDatabase().update(FeedReaderContract.FeedEntry.TABLE_NAME, values, "id = ? ", new String[] { Long.toString(id) } );
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
