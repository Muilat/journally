package com.android.muilat.journally.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {EntryClass.class}, version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class JournalDatabase extends RoomDatabase {

    private static final String LOG_TAG = JournalDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private  static final String DATABASE_NAME = "journally";
    private static JournalDatabase sInstance;

    public static JournalDatabase getsInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new Database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        JournalDatabase.class, JournalDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract JournalDao journalDao();

}
