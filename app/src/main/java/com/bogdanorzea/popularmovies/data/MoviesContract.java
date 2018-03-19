package com.bogdanorzea.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.bogdanorzea.popularmovies";
    public static final String PATH_MOVIE = "movie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE);

    // MIME types
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE + "/#";
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

    // SQL constants
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String SEPARATOR = ", ";
    private static final String TEXT = " TEXT";
    private static final String INTEGER = " INTEGER";
    private static final String REAL = " REAL";
    private static final String DEFAULT_0 = " DEFAULT 0";
    private static final String BLOB = " BLOB";

    // SQL statements
    static final String SQL_DELETE_MOVIES_TABLE =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
    static final String SQL_CREATE_MOVIES_TABLE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + INTEGER + PRIMARY_KEY + SEPARATOR +
                    MovieEntry.COLUMN_NAME_TITLE + TEXT + SEPARATOR +
                    MovieEntry.COLUMN_NAME_FAVORITE + INTEGER + DEFAULT_0 + SEPARATOR +
                    MovieEntry.COLUMN_NAME_RELEASE_DATE + TEXT + SEPARATOR +
                    MovieEntry.COLUMN_NAME_TAGLINE + TEXT + SEPARATOR +
                    MovieEntry.COLUMN_NAME_OVERVIEW + TEXT + SEPARATOR +
                    MovieEntry.COLUMN_NAME_GENDERS + TEXT + SEPARATOR +
                    MovieEntry.COLUMN_NAME_RUNTIME + INTEGER + SEPARATOR +
                    MovieEntry.COLUMN_NAME_POPULARITY + REAL + SEPARATOR +
                    MovieEntry.COLUMN_NAME_VOTE_AVERAGE + REAL + SEPARATOR +
                    MovieEntry.COLUMN_NAME_VOTE_COUNT + INTEGER + SEPARATOR +
                    MovieEntry.COLUMN_NAME_BUDGET + INTEGER + SEPARATOR +
                    MovieEntry.COLUMN_NAME_REVENUE + INTEGER + SEPARATOR +
                    MovieEntry.COLUMN_NAME_HOMEPAGE + TEXT + SEPARATOR +
                    MovieEntry.COLUMN_NAME_BACKDROP_PATH + TEXT + SEPARATOR +
                    MovieEntry.COLUMN_NAME_POSTER_PATH + TEXT +
                    ");";

    private MoviesContract() {
    }

    public static class MovieEntry implements BaseColumns {
        static final String TABLE_NAME = "movies";

        // Column names
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FAVORITE = "favorite";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_TAGLINE = "tagline";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_GENDERS = "genders";
        public static final String COLUMN_NAME_RUNTIME = "runtime";
        public static final String COLUMN_NAME_POPULARITY = "popularity";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_VOTE_COUNT = "vote_count";
        public static final String COLUMN_NAME_BUDGET = "budget";
        public static final String COLUMN_NAME_REVENUE = "revenue";
        public static final String COLUMN_NAME_HOMEPAGE = "homepage";
        public static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
    }

}
