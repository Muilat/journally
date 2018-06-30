package com.android.muilat.journally.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class JournalContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.android.muilat.journally";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "journals" directory
    public static final String PATH_JOURNALS = "journals";

    /* JournalEntry is an inner class that defines the contents of the journal table */
    public static final class JournalEntry implements BaseColumns {

        // JournalEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_JOURNALS).build();


        // Task table and column names
        public static final String TABLE_NAME = "journals";

        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_FEELINGS = "feelings";


    }
}