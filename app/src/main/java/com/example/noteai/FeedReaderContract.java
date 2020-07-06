package com.example.noteai;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    int REQUEST_PERMISSION_CODE = 1000;
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_BODY = "body";
    }


}

