package com.android.muilat.journally;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.muilat.journally.data.EntryClass;
import com.android.muilat.journally.data.JournalDatabase;

public class EntryDetailActivity extends AppCompatActivity {

    public static String EXTRA_JOURNAL_ID;
    private int mJournalId;
    private static int DEFAULT_ID = -1;
    private JournalDatabase mDb;

    private TextView tv_title, tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.tv_description);


        mDb = JournalDatabase.getsInstance(getApplicationContext());


        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)){
            mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID,DEFAULT_ID);


            ViewModelProvider.Factory factory = new AddJournalViewModelFactory(mDb, mJournalId);
            final AddEntryViewModel viewModel = ViewModelProviders.of(this, factory).get(AddEntryViewModel.class);
            viewModel.getJournal().observe(this, new Observer<EntryClass>() {
                @Override
                public void onChanged(@Nullable EntryClass entryClass) {
                    viewModel.getJournal().removeObserver(this);

                    //TODO: extract
                    tv_title.setText(entryClass.getTitle());
                    tv_description.setText(entryClass.getDescription());
                }
            });


        }
    }
}
