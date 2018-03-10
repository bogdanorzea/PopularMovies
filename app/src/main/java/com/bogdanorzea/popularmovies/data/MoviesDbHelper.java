package com.bogdanorzea.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.bogdanorzea.popularmovies.data.FavoritesContract.SQL_CREATE_FAVORITES_TABLE;
import static com.bogdanorzea.popularmovies.data.FavoritesContract.SQL_DELETE_FAVORITES_TABLE;


public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_FAVORITES_TABLE);
        onCreate(sqLiteDatabase);
    }

}
