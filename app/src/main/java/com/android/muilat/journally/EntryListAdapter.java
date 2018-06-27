package com.android.muilat.journally;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.muilat.journally.data.EntryClass;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.EntryViewHolder> {

    private Context mContext;
    private List<EntryClass> mJournals;


    private ItemClickListener mItemClickListener;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault());

    /**
     * Constructor using the context and the db cursor
     *
     * @param context the calling context/activity
     */
    public EntryListAdapter(Context context, ItemClickListener listerner) {
        this.mContext = context;
        this.mItemClickListener = listerner;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item
     *
     * @param parent   The ViewGroup into which the new View will be added
     * @param viewType The view type of the new View
     * @return A new EntryViewHolder that holds a View with the entry_list_item layout
     */
    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.entry_list_item, parent, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {

		EntryClass  entryClass = mJournals.get(position);
		
        holder.txtVw_description.setText(entryClass.getDescription());
        holder.txtVw_title.setText(entryClass.getTitle());
//
//        int imgRes = EntryUtils.getEntryImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, entryType);
//
//        holder.entryImageView.setImageResource(imgRes);
//        holder.entryNameView.setText(String.valueOf(entryId));
//        holder.entryImageView.setTag(entryId);
    }

	
	/*
    Helper method for selecting the correct priority circle color.
    P1 = red, P2 = orange, P3 = yellow
    */
    private int getPriorityColor(int priority) {
        int priorityColor = 0;

//        switch(priority) {
//            case 1: priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
//                break;
//            case 2: priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
//                break;
//            case 3: priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
//                break;
//            default: break;
//        }
        return priorityColor;
    }
	

    /**
     * Returns the number of items to display
     *
     */
    @Override
    public int getItemCount() {
        if (mJournals == null) return 0;
        return mJournals.size();
    }


    //update list when data changes
    public void setJournals(List<EntryClass> entryClasses){
        mJournals = entryClasses;
        notifyDataSetChanged();
    }

    public List<EntryClass> getJournals(){
        return mJournals;
    }

    public interface ItemClickListener{
        void onItemClickListener(int itemId);
    }

    /**
     * EntryViewHolder class for the recycler view item
     */
    class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtVw_title, txtVw_description;

        public EntryViewHolder(View itemView) {
            super(itemView);
//            txtVw_title = (ImageView) itemView.findViewById(R.id.txtVw_title);
//            txtVw_description = (TextView) itemView.findViewById(R.id.txtVw_description);
        }

    }
}