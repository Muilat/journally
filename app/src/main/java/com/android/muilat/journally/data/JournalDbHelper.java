package com.android.muilat.journally.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.muilat.journally.data.JournalContract.JournalEntry;


public class JournalDbHelper  extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "journally.db";

    private static final int VERSION = 1;


    // Constructor
    JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + JournalEntry.TABLE_NAME + " (" +
                JournalEntry._ID                + " INTEGER PRIMARY KEY, " +
                JournalEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                JournalEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                JournalEntry.COLUMN_FEELINGS + " INT NOT NULL, " +
                JournalEntry.COLUMN_DATE    + " LONG NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME);
        onCreate(db);
    }
}

