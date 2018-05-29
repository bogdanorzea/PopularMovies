package com.bogdanorzea.popularmovies.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns
import com.bogdanorzea.popularmovies.data.MoviesContract.MovieEntry.TABLE_NAME

object MoviesContract {

    const val CONTENT_AUTHORITY = "com.bogdanorzea.popularmovies"
    const val PATH_MOVIE = "movie"

    private val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")
    @JvmField
    val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE)

    // MIME types
    const val CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE + "/#"
    const val CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE

    // SQL statements
    internal const val SQL_DELETE_MOVIES_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    internal const val SQL_CREATE_MOVIES_TABLE = "CREATE TABLE $TABLE_NAME (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
            MovieEntry.COLUMN_NAME_FAVORITE + " INTEGER DEFAULT 0, " +
            MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
            MovieEntry.COLUMN_NAME_TAGLINE + " TEXT, " +
            MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
            MovieEntry.COLUMN_NAME_GENDERS + " TEXT, " +
            MovieEntry.COLUMN_NAME_RUNTIME + " INTEGER, " +
            MovieEntry.COLUMN_NAME_POPULARITY + " REAL, " +
            MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL, " +
            MovieEntry.COLUMN_NAME_VOTE_COUNT + " INTEGER, " +
            MovieEntry.COLUMN_NAME_BUDGET + " INTEGER, " +
            MovieEntry.COLUMN_NAME_REVENUE + " INTEGER, " +
            MovieEntry.COLUMN_NAME_HOMEPAGE + " TEXT, " +
            MovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT, " +
            MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT);"

    object MovieEntry {
        internal const val TABLE_NAME = "movies"

        const val _ID = BaseColumns._ID
        const val _COUNT = BaseColumns._COUNT
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_FAVORITE = "favorite"
        const val COLUMN_NAME_RELEASE_DATE = "release_date"
        const val COLUMN_NAME_TAGLINE = "tagline"
        const val COLUMN_NAME_OVERVIEW = "overview"
        const val COLUMN_NAME_GENDERS = "genders"
        const val COLUMN_NAME_RUNTIME = "runtime"
        const val COLUMN_NAME_POPULARITY = "popularity"
        const val COLUMN_NAME_VOTE_AVERAGE = "vote_average"
        const val COLUMN_NAME_VOTE_COUNT = "vote_count"
        const val COLUMN_NAME_BUDGET = "budget"
        const val COLUMN_NAME_REVENUE = "revenue"
        const val COLUMN_NAME_HOMEPAGE = "homepage"
        const val COLUMN_NAME_BACKDROP_PATH = "backdrop_path"
        const val COLUMN_NAME_POSTER_PATH = "poster_path"
    }

}
