package com.android.muilat.journally;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.muilat.journally.data.EntryClass;
import com.android.muilat.journally.data.JournalContract;

import java.util.Date;

import static com.android.muilat.journally.EntryDetailActivity.EXTRA_JOURNAL_ID;

public class AddEntryActivity extends AppCompatActivity {

    private static final int DEFAULT_ID = -1;
    public static String EXTRA_TASK_ID ;
    EditText edtTxt_title, edtTxt_description;

    FeelingsAdapter mFeelingsAdapter;
    RecyclerView mFeelingsRecyclerView;

    //manipulate feeling selections
    TextView curr_feeling;

    int mFeelings;
    int mJournalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        // feelings types are displayed as a recycler view using FeelingsAdapter
        mFeelingsAdapter = new FeelingsAdapter(this);
        mFeelingsRecyclerView = findViewById(R.id.feelings_recycler_view);
        mFeelingsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mFeelingsRecyclerView.setAdapter(mFeelingsAdapter);

        edtTxt_title = findViewById(R.id.edtTxt_title);
        edtTxt_description = findViewById(R.id.edtTxt_description);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_ID);
            String stringId = Long.toString(mJournalId);
            Uri uri = JournalContract.JournalEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();

            // query a single row of data using a ContentResolver
            Cursor mCursor = getContentResolver().query(uri, null, null, null, null);
            mCursor.moveToNext();

            String journal_title = mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE));
            String journal_description = mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DESCRIPTION));
            int journal_feelings = mCursor.getInt(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_FEELINGS));
            long journal_date = mCursor.getLong(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DATE));

            edtTxt_title.setText(journal_title);
            edtTxt_description.setText(journal_description);

            mFeelings = journal_feelings;



        }



        }

    public void onAddButtonClick(View view) {
        String journal_title, journal_description;
        Date created_at = new Date();

        journal_description = edtTxt_description.getText().toString();
        journal_title = edtTxt_title.getText().toString();

        if(mFeelings < 0){
            Toast.makeText(this, R.string.select_icon, Toast.LENGTH_SHORT).show();
            return;
        }
        if(journal_title.equals("")){
            Toast.makeText(this, R.string.enter_journal_title, Toast.LENGTH_SHORT).show();
            return;
        }
        if(journal_description.equals("")){
            Toast.makeText(this, R.string.enter_journal_description, Toast.LENGTH_SHORT).show();
            return;
        }

     // Insert new journal data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the journal  into the ContentValues
        contentValues.put(JournalContract.JournalEntry.COLUMN_DESCRIPTION, journal_description);
        contentValues.put(JournalContract.JournalEntry.COLUMN_TITLE, journal_title);

        contentValues.put(JournalContract.JournalEntry.COLUMN_FEELINGS, mFeelings);
        // Insert the content values via a ContentResolver
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
//            ActionBar actionBar = getActionBar();
//            actionBar.setTitle(R.string.edit_journal_entry+"");

            // Build appropriate uri with String row id appended
            String stringId = Long.toString(mJournalId);
            Uri uri = JournalContract.JournalEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
             int id = getContentResolver().update(uri, contentValues,null, null);
            if (id != 0){
                Toast.makeText(getBaseContext(), getString(R.string.entry_updated), Toast.LENGTH_LONG).show();

            }
        }
        else {

//            ActionBar actionBar = getActionBar();
//            actionBar.setTitle(R.string.add_newJounal_entry+"");


            contentValues.put(JournalContract.JournalEntry.COLUMN_DATE, created_at.getTime());
            Uri uri = getContentResolver().insert(JournalContract.JournalEntry.CONTENT_URI, contentValues);
            if(uri != null) {
                Toast.makeText(getBaseContext(), journal_title+" "+getString(R.string.added_succesful), Toast.LENGTH_LONG).show();
            }
        }



        // Finish activity (this returns back to MainActivity)
        finish();



    }

    public void onFeelingClick(View view){
        ImageView imgView =  view.findViewById(R.id.feelings_image);
        int feelingType = (int) imgView.getTag();

        if(curr_feeling !=null){
            //a feeling alredy selected, deselect
            curr_feeling.setVisibility(View.GONE);
        }

        TextView selected = view.findViewById(R.id.selection_holder);
        selected.setVisibility(View.VISIBLE);

        curr_feeling = selected;

        mFeelings = feelingType;

    }
}
