package com.android.muilat.journally.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.android.muilat.journally.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalUtils {
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM");


    private static final long MINUTE_MILLIS = 1000 * 60;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    public static int getFeelingImgRes(Context context, int type) {
        Resources res = context.getResources();
        TypedArray feelings = res.obtainTypedArray(R.array.feelings);
        String resName = feelings.getString(type);
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }

    /**
     * Returns the plant type display name based on the type index from the string resources
     *
     * @param context The context
     * @param type    The plant type (starts from 0 and corresponds to the index to the item in arrays.xml)
     * @return The plant type display name
     */
    public static String getFeelingName(Context context, int type) {
        Resources res = context.getResources();
        TypedArray feelings = res.obtainTypedArray(R.array.feelings);
        String resName = feelings.getString(type);
        int resId = context.getResources().getIdentifier(resName, "string", context.getPackageName());
        try {
            return context.getResources().getString(resId);
        } catch (Resources.NotFoundException ex) {
            return context.getResources().getString(R.string.unknown_feeling);
        }
    }

    public static String dateFormatter(long dateMillis) {
        long now = System.currentTimeMillis();

        String date;
        if (now - dateMillis < (DAY_MILLIS)) {
            if (now - dateMillis < (HOUR_MILLIS)) {
                long minutes = Math.round((now - dateMillis) / MINUTE_MILLIS);
                date = String.valueOf(minutes) + "m";
            } else {
                long minutes = Math.round((now - dateMillis) / HOUR_MILLIS);
                date = String.valueOf(minutes) + "h";
            }
        } else {
            Date dateDate = new Date(dateMillis);
            date = sDateFormat.format(dateDate);
        }
        // Add a dot to the date string
        date = "\u2022 " + date;
        return date;
    }
}
