package com.android.muilat.journally;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

import com.android.muilat.journally.data.EntryClass;
import com.android.muilat.journally.data.JournalDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements EntryListAdapter.ItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    JournalDatabase mDb;
    EntryListAdapter mAdapter;
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = JournalDatabase.getsInstance((getApplicationContext()));

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.journals_list_recycler_view);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new EntryListAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

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
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        int position = viewHolder.getAdapterPosition();
//                        List<TaskEntry> tasks = mAdapter.getTasks();
//                        mDb.taskDao().deleteTask(tasks.get(position));
//                    }
//                });
            }
        }).attachToRecyclerView(mRecyclerView);

        //retrieve data
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getJournals().observe(this, new Observer<List<EntryClass>>() {
            @Override
            public void onChanged(@Nullable List<EntryClass> entryClasses) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setJournals(entryClasses);
            }
        });
    }


    public void onAddFabClick(View view) {
        Intent addJournalIntent = new Intent(this, AddEntryActivity.class);
        startActivity(addJournalIntent);
    }

    @Override
    public void onItemClickListener(int itemId) {
        // Launch EntryDetailActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(MainActivity.this, EntryDetailActivity.class);
        intent.putExtra(EntryDetailActivity.EXTRA_JOURNAL_ID, itemId);
        startActivity(intent);
    }
}
