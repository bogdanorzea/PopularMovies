package com.bogdanorzea.popularmovies.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.bogdanorzea.popularmovies.data.MoviesContract.CONTENT_AUTHORITY
import com.bogdanorzea.popularmovies.data.MoviesContract.MovieEntry
import com.bogdanorzea.popularmovies.data.MoviesContract.PATH_MOVIE
import timber.log.Timber

class MovieProvider : ContentProvider() {

    companion object {

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        private const val MOVIES = 1
        private const val MOVIE_ID = 2

        init {
            uriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE, MOVIES)
            uriMatcher.addURI(CONTENT_AUTHORITY, "$PATH_MOVIE/#", MOVIE_ID)
        }
    }

    private lateinit var moviesDbHelper: MoviesDbHelper

    override fun onCreate(): Boolean {
        moviesDbHelper = MoviesDbHelper(context)

        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var tempSelection = selection
        var tempSelectionArgs = selectionArgs
        val db: SQLiteDatabase = moviesDbHelper.readableDatabase

        val cursor: Cursor
        when (uriMatcher.match(uri)) {
            MOVIES -> cursor = db.query(MovieEntry.TABLE_NAME, projection, tempSelection, tempSelectionArgs, null, null, sortOrder)
            MOVIE_ID -> {
                tempSelection = MoviesContract.MovieEntry._ID + " = ?"
                tempSelectionArgs = arrayOf(ContentUris.parseId(uri).toString())

                cursor = db.query(MovieEntry.TABLE_NAME, projection, tempSelection, tempSelectionArgs, null, null, sortOrder)
            }
            else -> throw IllegalArgumentException("Cannot query unknown Uri $uri")
        }

        cursor.setNotificationUri(context.contentResolver, uri)

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            MOVIES -> MoviesContract.CONTENT_LIST_TYPE
            MOVIE_ID -> MoviesContract.CONTENT_ITEM_TYPE
            else -> throw IllegalStateException("Unknown match for Uri $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (uriMatcher.match(uri)) {
            MOVIES -> insertMovie(uri, values)
            else -> throw IllegalArgumentException("Cannot insert unknown Uri $uri")
        }
    }

    private fun insertMovie(uri: Uri, contentValues: ContentValues?): Uri? {
        if (contentValues == null)
            throw IllegalArgumentException("Invalid values to insert for Uri $uri")

        val movieId: Int = contentValues.getAsInteger(MoviesContract.MovieEntry._ID)
        if (movieId < 0)
            throw IllegalArgumentException("Invalid movie id")

        val db: SQLiteDatabase = moviesDbHelper.writableDatabase
        val newRowId = db.insertWithOnConflict(MovieEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE)

        if (newRowId < 0) {
            Timber.e("Failed to insert %s for Uri %s", movieId, uri)
            return null
        }

        context.contentResolver.notifyChange(uri, null)

        return ContentUris.withAppendedId(uri, newRowId)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var tempSelection = selection
        var tempSelectionArgs = selectionArgs
        val database: SQLiteDatabase = moviesDbHelper.writableDatabase

        val rowsDeleted: Int
        when (uriMatcher.match(uri)) {
            MOVIES -> rowsDeleted = database.delete(MovieEntry.TABLE_NAME, tempSelection, tempSelectionArgs)
            MOVIE_ID -> {
                tempSelection = MoviesContract.MovieEntry._ID + " = ?"
                tempSelectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, tempSelection, tempSelectionArgs)
            }
            else -> throw IllegalStateException("Deletion is not supported for Uri $uri")
        }

        if (rowsDeleted != 0) {
            context.contentResolver.notifyChange(uri, null)
        }

        return rowsDeleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var tempSelection = selection
        var tempSelectionArgs = selectionArgs

        return when (uriMatcher.match(uri)) {
            MOVIES -> updateMovie(uri, values, tempSelection, tempSelectionArgs)
            MOVIE_ID -> {
                tempSelection = MoviesContract.MovieEntry._ID + " = ?"
                tempSelectionArgs = arrayOf(ContentUris.parseId(uri).toString())

                updateMovie(uri, values, tempSelection, tempSelectionArgs)
            }
            else -> throw IllegalArgumentException("Cannot update unknown Uri $uri")
        }
    }

    private fun updateMovie(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgument: Array<String>?): Int {
        if (contentValues == null)
            throw IllegalArgumentException("Invalid values to update the Uri $uri")

        if (contentValues.containsKey(MoviesContract.MovieEntry._ID)) {
            val movieId = contentValues.getAsInteger(MoviesContract.MovieEntry._ID) ?: -1
            if (movieId < 0)
                throw IllegalArgumentException("Invalid movie id")
        }

        val db: SQLiteDatabase = moviesDbHelper.writableDatabase

        val rowsAffected = db.update(MovieEntry.TABLE_NAME, contentValues, selection, selectionArgument)

        if (rowsAffected != 0) {
            context.contentResolver.notifyChange(uri, null)
        }

        return rowsAffected
    }

}
