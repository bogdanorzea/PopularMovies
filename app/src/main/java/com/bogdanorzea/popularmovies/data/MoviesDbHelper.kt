package com.bogdanorzea.popularmovies.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bogdanorzea.popularmovies.data.MoviesContract.MovieEntry.TABLE_NAME
import com.bogdanorzea.popularmovies.data.MoviesContract.SQL_CREATE_MOVIES_TABLE
import com.bogdanorzea.popularmovies.data.MoviesContract.SQL_DELETE_MOVIES_TABLE
import timber.log.Timber


internal class MoviesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "movies.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        Timber.i("%s table created using statement: %s", TABLE_NAME, SQL_CREATE_MOVIES_TABLE)
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        Timber.i("%s table deleted using statement: %s", TABLE_NAME, SQL_DELETE_MOVIES_TABLE)
        sqLiteDatabase.execSQL(SQL_DELETE_MOVIES_TABLE)
        onCreate(sqLiteDatabase)
    }

}
