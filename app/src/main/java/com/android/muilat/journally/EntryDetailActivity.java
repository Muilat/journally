package com.android.muilat.journally;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.muilat.journally.data.JournalContract;
import com.android.muilat.journally.utils.JournalUtils;

public class EntryDetailActivity extends AppCompatActivity {

    public static String EXTRA_JOURNAL_ID;
    private int mJournalId;
    private static int DEFAULT_ID = -1;

    private TextView tv_title, tv_description, tv_date;
    private ImageView iv_feelings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.tv_description);
        tv_date = findViewById(R.id.tv_date);
        iv_feelings = findViewById(R.id.iv_detail_feelings);

        getSupportActionBar().setTitle(R.string.journal_entry_detail);



    }

    private void getJournal() {

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)){
            Uri uri = buildUri(intent);

            // query a single row of data using a ContentResolver
            Cursor mCursor = getContentResolver().query(uri,null,null,null,null);
            mCursor.moveToNext();

            String journal_title = mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE));
            String journal_description = mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DESCRIPTION));
            int journal_feelings = mCursor.getInt(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_FEELINGS));
            long journal_date = mCursor.getLong(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DATE));

            tv_title.setText(journal_title);
            tv_description.setText(journal_description);

            int imgRes = JournalUtils.getFeelingImgRes(
                    this, journal_feelings);
            iv_feelings.setImageResource(imgRes);

            tv_date.setText(JournalUtils.dateFormatter(journal_date));

        }
    }

    private Uri buildUri(Intent intent) {
        mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID,DEFAULT_ID);
        String stringId = Long.toString(mJournalId);
        Uri uri = JournalContract.JournalEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        return uri;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getJournal();
    }

    public void onEditFabClick(View view) {
        // Create a new intent to start an AddEntryActivity
        Intent journalDetailIntent = new Intent(EntryDetailActivity.this, AddEntryActivity.class);
        journalDetailIntent.putExtra(EntryDetailActivity.EXTRA_JOURNAL_ID, mJournalId);
        startActivity(journalDetailIntent);
    }

    public void onDeleteFabClick(View view) {
        Intent intent = getIntent();

        Uri uri = buildUri(intent);
        getContentResolver().delete(uri, null, null);
        Toast.makeText(this, R.string.journal_entry_deleted, Toast.LENGTH_SHORT).show();
        Intent mainActivityIntent = new Intent(EntryDetailActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
    }


}
