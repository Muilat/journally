package com.android.muilat.journally;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.android.muilat.journally.data.EntryClass;
import com.android.muilat.journally.data.JournalDatabase;

import java.util.Date;

public class AddEntryActivity extends AppCompatActivity {

    public static String EXTRA_TASK_ID ;
    EditText edtTxt_title, edtTxt_description;

    private JournalDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        edtTxt_title = findViewById(R.id.edtTxt_title);
        edtTxt_description = findViewById(R.id.edtTxt_description);

        mDb = JournalDatabase.getsInstance(getApplicationContext());
    }

    public void onAddButtonClick(View view) {
        String journal_title, journal_description;
        Date created_at = new Date();

        journal_description = edtTxt_description.getText().toString();
        journal_title = edtTxt_title.getText().toString();

        EntryClass entryClass = new EntryClass(journal_title, journal_description, created_at);

        mDb.journalDao().insertJournal(entryClass);



    }
}
