package com.bogdanorzea.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

import static com.bogdanorzea.popularmovies.data.MoviesContract.MovieEntry.TABLE_NAME;
import static com.bogdanorzea.popularmovies.data.MoviesContract.SQL_CREATE_MOVIES_TABLE;
import static com.bogdanorzea.popularmovies.data.MoviesContract.SQL_DELETE_MOVIES_TABLE;


public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Timber.i("%s table created using statement: %s", TABLE_NAME, SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Timber.i("%s table deleted using statement: %s", TABLE_NAME, SQL_DELETE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_MOVIES_TABLE);
        onCreate(sqLiteDatabase);
    }

}
