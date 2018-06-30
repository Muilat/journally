package com.android.muilat.journally;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.muilat.journally.utils.JournalUtils;

import static com.android.muilat.journally.utils.JournalUtils.getFeelingImgRes;
import static com.android.muilat.journally.utils.JournalUtils.getFeelingName;


public class FeelingsAdapter extends RecyclerView.Adapter<FeelingsAdapter.FeelingViewHolder> {

    Context mContext;
    static TypedArray mFeelings;

    /**
     * Constructor using the context and the db cursor
     *
     * @param context the calling context/activity
     */
    public FeelingsAdapter(Context context) {
        mContext = context;
        Resources res = mContext.getResources();
        mFeelings = res.obtainTypedArray(R.array.feelings);
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item
     *
     * @param parent   The ViewGroup into which the new View will be added
     * @param viewType The view type of the new View
     * @return A new FeelingViewHolder that holds a View with the feeling_list_item layout
     */
    @Override
    public FeelingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.feelings_list_item, parent, false);
        return new FeelingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeelingViewHolder holder, int position) {
        int imgRes = JournalUtils.getFeelingImgRes(
                mContext, position);
        holder.feelingImageView.setImageResource(imgRes);
//        setFeelingImage(holder.feelingImageView, position);
//        Toast.makeText(mContext, imgRes+"", Toast.LENGTH_SHORT).show();
        holder.feelingText.setText(JournalUtils.getFeelingName(mContext, position));
        holder.feelingImageView.setTag(position);
    }

    /**
     * Returns the number of items in the cursor
     *
     * @return Number of items in the cursor, or 0 if null
     */
    @Override
    public int getItemCount() {
        if (mFeelings == null) return 0;
        return mFeelings.length();
    }

    /**
     * FeelingViewHolder class for the recycler view item
     */
    class FeelingViewHolder extends RecyclerView.ViewHolder {

        ImageView feelingImageView;
        TextView feelingText;

        public FeelingViewHolder(View itemView) {
            super(itemView);
            feelingImageView = itemView.findViewById(R.id.feelings_image);
            feelingText = itemView.findViewById(R.id.feelings_text);
        }

    }





}
