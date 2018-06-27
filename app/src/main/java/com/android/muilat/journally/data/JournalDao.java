package com.android.muilat.journally.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface JournalDao {
    @Query("SELECT * FROM journals ORDER BY created_at")
    LiveData<List<EntryClass>> loadAllJournals();

    @Insert
    void insertJournal(EntryClass entryClass);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournal(EntryClass entryClass);

    @Delete
    void deleteJournal(EntryClass entryClass);

    //query for one entry
    @Query("SELECT * FROM journals WHERE id = :journalId")
    LiveData<EntryClass> loadJournalById(int journalId);

}
