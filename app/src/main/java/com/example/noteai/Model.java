package com.example.noteai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Model extends SQLiteOpenHelper {

    public Model(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.i("notedev","dbcreate");
        sqLiteDatabase.execSQL(
                "create table "+FeedReaderContract.FeedEntry.TABLE_NAME +
                        " ("+FeedReaderContract.FeedEntry.MAIN_id+" integer primary key, "+FeedReaderContract.FeedEntry.COLUMN_NAME+" text, "+FeedReaderContract.FeedEntry.COLUMN_NAME_2+" BOOLEAN NOT NULL CHECK ("+FeedReaderContract.FeedEntry.COLUMN_NAME_2+" IN (0,1)))"
        );

        sqLiteDatabase.execSQL(
                "create table "+FeedReaderContract.FeedEntry.CHILD_TABLE_NAME +
                        " ("+FeedReaderContract.FeedEntry.CHILD_id+" integer primary key, "+FeedReaderContract.FeedEntry.CHILD_COLUMN_NAME+" text, "+
                        FeedReaderContract.FeedEntry.MAIN_id+" INTEGER NOT NULL,"+
                        "FOREIGN KEY ("+FeedReaderContract.FeedEntry.MAIN_id+") REFERENCES "+FeedReaderContract.FeedEntry.TABLE_NAME+"("+FeedReaderContract.FeedEntry.MAIN_id+"))"
        );
    }

    // To read from Database
    public Cursor fetchAll(){
        String[] projection = {
                FeedReaderContract.FeedEntry.MAIN_id,
                FeedReaderContract.FeedEntry.COLUMN_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_2,
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
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME, text);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_2, 1);
        long newRowId = this.getWritableDatabase().insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

        return newRowId;
    }


    public void updateNote(long id, String text) {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME, text);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_2, 1);
        this.getWritableDatabase().update(FeedReaderContract.FeedEntry.TABLE_NAME, values, FeedReaderContract.FeedEntry.MAIN_id+" = ? ", new String[] { Long.toString(id) } );
    }

    public Integer deleteNote (Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.MAIN_id+" = ? ",
                new String[] { Long.toString(id) });
    }

    public Boolean getTextChange(long rowid){
        boolean result=false;
        String[] projection = {
                FeedReaderContract.FeedEntry.MAIN_id,
                FeedReaderContract.FeedEntry.COLUMN_NAME_2
        };
        Cursor cursor = this.getReadableDatabase().query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                FeedReaderContract.FeedEntry.MAIN_id+" =?",              // The columns for the WHERE clause
                new String[] { Long.toString(rowid) },          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while(cursor.moveToNext()) {

            if(cursor.getLong(1)==1){
                result = true;
            }else{
                result = false;
            }
        }
        cursor.close();
        return result;
    }

    public void setTextChange(long rowid,boolean status){
        String res;
        if(status){
            res = "1";
        }else{
            res = "0";
        }

         this.getWritableDatabase().execSQL("UPDATE "+FeedReaderContract.FeedEntry.TABLE_NAME+
                                            " SET "+FeedReaderContract.FeedEntry.COLUMN_NAME_2+" = "+res+
                                            " WHERE "+FeedReaderContract.FeedEntry.MAIN_id+" ="+Long.toString(rowid));
    }

    public Long insertChild(long rowId){
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.CHILD_COLUMN_NAME, "Hey Bro");
        values.put(FeedReaderContract.FeedEntry.MAIN_id, rowId);
        long newRowId = this.getWritableDatabase().insert(FeedReaderContract.FeedEntry.CHILD_TABLE_NAME, null, values);

        return newRowId;
    }



    // To read from Database
    public Cursor fetchChildAll(){
        String[] projection = {
                FeedReaderContract.FeedEntry.CHILD_id,
                FeedReaderContract.FeedEntry.CHILD_COLUMN_NAME,
                FeedReaderContract.FeedEntry.MAIN_id
        };
        Cursor cursor = this.getReadableDatabase().query(
                FeedReaderContract.FeedEntry.CHILD_TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        return cursor;
    }





    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
