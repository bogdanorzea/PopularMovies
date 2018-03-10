package com.bogdanorzea.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.bogdanorzea.popularmovies";
    public static final String PATH_FAVORITE = "favorite";
    private static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String INTEGER = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";

    static final String SQL_DELETE_FAVORITES_TABLE =
            "DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME;
    static final String SQL_CREATE_FAVORITES_TABLE =
            "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                    FavoriteEntry._ID + INTEGER + PRIMARY_KEY + ")";

    private FavoritesContract() {
    }

    public static class FavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITE);
        public static final String TABLE_NAME = "favorites";
    }

}
