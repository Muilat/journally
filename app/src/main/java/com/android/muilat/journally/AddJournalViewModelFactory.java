package com.android.muilat.journally;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.android.muilat.journally.data.JournalDatabase;

public class AddJournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private  final JournalDatabase mDb;
    private  final int mJournalId;

    public AddJournalViewModelFactory(JournalDatabase mDb, int journalId) {
        this.mDb = mDb;
        this.mJournalId = journalId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddEntryViewModel(mDb, mJournalId);
    }
}
