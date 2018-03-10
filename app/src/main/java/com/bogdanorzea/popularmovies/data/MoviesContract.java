package com.bogdanorzea.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.bogdanorzea.popularmovies";
    public static final String PATH_MOVIE = "favorite";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE);

    // MIME types
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE + "/#";
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

    // SQL constants
    private static final String INTEGER = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String SEPARATOR = ",";
    private static final String TEXT = " TEXT";
    private static final String DEFAULT_0 = " DEFAULT 0";

    // SQL statements
    static final String SQL_DELETE_FAVORITES_TABLE =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
    static final String SQL_CREATE_FAVORITES_TABLE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + INTEGER + PRIMARY_KEY + SEPARATOR +
                    MovieEntry.COLUMN_NAME_TITLE + TEXT + SEPARATOR +
                    MovieEntry.COLUMN_NAME_FAVORITE + INTEGER + DEFAULT_0 +
                    ");";

    private MoviesContract() {
    }

    public static class MovieEntry implements BaseColumns {
        static final String TABLE_NAME = "favorites";

        // Column names
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FAVORITE = "favorite";
    }

}
