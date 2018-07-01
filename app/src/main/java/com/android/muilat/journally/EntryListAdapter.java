package com.android.muilat.journally;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.muilat.journally.data.JournalContract;
import com.android.muilat.journally.utils.JournalUtils;


/**
 * This EntryListAdapter creates and binds ViewHolders, that hold the description and priority of a journal,
 * to a RecyclerView to efficiently display data.
 */
public class 
EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.JournalViewHolder> {

    // Class variables for the Cursor that holds journal data and the Context
    private Cursor mCursor;
    private Context mContext;



    /**
     * Constructor for the EntryListAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public EntryListAdapter(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new JournalViewHolder that holds the view for each journal
     */
    @Override
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the journal_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.entry_list_item, parent, false);
        final JournalViewHolder viewHolder = new JournalViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = (int)viewHolder.itemView.getTag();
                // Create a new intent to start an EntryDetailActivity
                Intent journalDetailIntent = new Intent(mContext, EntryDetailActivity.class);
                journalDetailIntent.putExtra(EntryDetailActivity.EXTRA_JOURNAL_ID, id);
                mContext.startActivity(journalDetailIntent);
            }
        });
        return viewHolder;
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(JournalContract.JournalEntry._ID);
        int titleIndex = mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE);
        int descriptionIndex = mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DESCRIPTION);
        int dateIndex = mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DATE);
        int feelingIndex = mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_FEELINGS);




        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String title = mCursor.getString(titleIndex);
        String description = mCursor.getString(descriptionIndex);
        long dateMillis = mCursor.getLong(dateIndex);
        int feelingsId = mCursor.getInt(feelingIndex);

        // Get the date for displaying
        String date = "";

        // Change how the date is displayed depending on whether it was written in the last minute,
        // the hour, etc.
        date = JournalUtils.dateFormatter(dateMillis);



        //Set values
        holder.itemView.setTag(id);
        if(description.length() >50 ){
            description = description.substring(0,47)+"...";
        }
        if(title.length() >30 ){
            title = title.substring(0,27)+"...";
        }
        holder.descriptionView.setText(description);
        holder.titleView.setText(title);
        holder.dateView.setText(date);

        Resources res = mContext.getResources();
//        feelings.getS
        TypedArray feelings = res.obtainTypedArray(R.array.feelings);

        String resName = feelings.getString(feelingsId);
        int imgRes = mContext.getResources().getIdentifier(resName, "drawable", mContext.getPackageName());
        holder.iv_feelings.setImageResource(imgRes);


    }



    /*
    Helper method for selecting the correct priority circle color.
    P1 = red, P2 = orange, P3 = yellow
    */

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    // Inner class for creating ViewHolders
    class JournalViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the journal description and priority TextViews
        TextView descriptionView;
        TextView titleView;
        TextView dateView;
        ImageView iv_feelings;

        /**
         * Constructor for the JournalViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public JournalViewHolder(View itemView) {
            super(itemView);

            descriptionView =  itemView.findViewById(R.id.journal_description);
            titleView =  itemView.findViewById(R.id.journal_title);
            dateView =  itemView.findViewById(R.id.journal_date);
            iv_feelings = itemView.findViewById(R.id.iv_feelings);
        }
    }
}