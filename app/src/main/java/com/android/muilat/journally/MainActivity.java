package com.android.muilat.journally;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.muilat.journally.data.JournalContract;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

		public static final int RC_SIGN_IN = 1010;

    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int JOURNAL_LOADER_ID = 0;

    // Member variables for the adapter and RecyclerView
    private EntryListAdapter mAdapter;
    RecyclerView mRecyclerView;
	


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


		// Set the RecyclerView to its corresponding view
        mRecyclerView =  findViewById(R.id.journals_list_recycler_view);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new EntryListAdapter(this);
		mRecyclerView.setAdapter(mAdapter);


        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
               // Retrieve the id of the journal to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build appropriate uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = JournalContract.JournalEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                //get entry incase of undo
                 final Cursor toDeleteCursor = getContentResolver().query(uri,null,null,null,null);
                toDeleteCursor.moveToNext();
                final String journal_title = toDeleteCursor.getString(toDeleteCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE));

                 //delete
                getContentResolver().delete(uri, null, null);
                Snackbar snackbar = Snackbar.make(mRecyclerView, journal_title+ "deleted from favorites!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //resave the item to db


                        String journal_description = toDeleteCursor.getString(toDeleteCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DESCRIPTION));
                        int journal_feelings = toDeleteCursor.getInt(toDeleteCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_FEELINGS));
                        long journal_date = toDeleteCursor.getLong(toDeleteCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DATE));

                        ContentValues contentValues = new ContentValues();
                        // Put the journal  into the ContentValues
                        contentValues.put(JournalContract.JournalEntry.COLUMN_DESCRIPTION, journal_description);
                        contentValues.put(JournalContract.JournalEntry.COLUMN_TITLE, journal_title);
                        contentValues.put(JournalContract.JournalEntry.COLUMN_FEELINGS, journal_feelings);
                        contentValues.put(JournalContract.JournalEntry.COLUMN_DATE, journal_date);

                        // Insert the content values via a ContentResolver
                        Uri uri = getContentResolver().insert(JournalContract.JournalEntry.CONTENT_URI, contentValues);
                        if(uri != null) {
                            Toast.makeText(getBaseContext(), "Deletion undone", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimaryDark));
                snackbar.show();

                getSupportLoaderManager().restartLoader(JOURNAL_LOADER_ID, null, MainActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);


        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(JOURNAL_LOADER_ID, null, this);
		

    }



    public void onAddFabClick(View view) {
        // Create a new intent to start an AddJournalActivity
        Intent addJournalIntent = new Intent(MainActivity.this, AddEntryActivity.class);
        startActivity(addJournalIntent);
    }


    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, MainActivity.class);
        return in;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all journals
        getSupportLoaderManager().restartLoader(JOURNAL_LOADER_ID, null, this);
    }

	@Override
    protected void onPause() {
        super.onPause();

//
    }
	
	

    /**
     * Instantiates and returns a new AsyncJournalLoader with the given ID.
     * This loader will return journal data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the journal data
            Cursor mJournalData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mJournalData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mJournalData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all journal data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(JournalContract.JournalEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            JournalContract.JournalEntry.COLUMN_DATE+" DESC");

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mJournalData = data;
                super.deliverResult(data);
            }
        };

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
//                AuthUI.getInstance().signOut(this);
                startActivity(SignInActivity.createIntent(this));

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

