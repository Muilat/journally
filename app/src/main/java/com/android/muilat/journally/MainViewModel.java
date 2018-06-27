package com.android.muilat.journally;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.android.muilat.journally.data.EntryClass;
import com.android.muilat.journally.data.JournalDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<EntryClass>> entryClasses;

    public MainViewModel(@NonNull Application application) {
        super(application);

        JournalDatabase database =  JournalDatabase.getsInstance(this.getApplication());
        entryClasses = database.journalDao().loadAllJournals();


    }

    public LiveData<List<EntryClass>> getJournals() {
        return entryClasses;
    }
}
