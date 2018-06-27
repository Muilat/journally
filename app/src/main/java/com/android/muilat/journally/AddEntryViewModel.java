package com.android.muilat.journally;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.android.muilat.journally.data.EntryClass;
import com.android.muilat.journally.data.JournalDatabase;

class AddEntryViewModel extends ViewModel{

    private LiveData<EntryClass> entryClass;

    public AddEntryViewModel(JournalDatabase database, int journalId) {
        entryClass = database.journalDao().loadJournalById(journalId)
    }

    public LiveData<EntryClass> getJournal() {
        return entryClass;
    }
}
