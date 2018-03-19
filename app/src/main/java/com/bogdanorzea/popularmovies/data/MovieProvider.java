package com.bogdanorzea.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bogdanorzea.popularmovies.data.MoviesContract.MovieEntry;

import timber.log.Timber;

import static com.bogdanorzea.popularmovies.data.MoviesContract.CONTENT_AUTHORITY;
import static com.bogdanorzea.popularmovies.data.MoviesContract.PATH_MOVIE;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE, MOVIES);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE + "/#", MOVIE_ID);
    }

    private MoviesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                cursor = db.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown Uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return MoviesContract.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MoviesContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown match for Uri " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return insertMovie(uri, values);
            default:
                throw new IllegalArgumentException("Cannot insert unknown Uri " + uri);
        }
    }

    private Uri insertMovie(Uri uri, ContentValues contentValues) {
        Integer movieId = contentValues.getAsInteger(MovieEntry._ID);
        if (movieId < 0) {
            throw new IllegalArgumentException("Invalid movie id");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = db.insertWithOnConflict(MovieEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        if (newRowId < 0) {
            Timber.e("Failed to insert %s for Uri %s", movieId, uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalStateException("Deletion is not supported for uri " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return updateMovie(uri, values, selection, selectionArgs);

            case MOVIE_ID:
                selection = MovieEntry._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                return updateMovie(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Cannot update unknown Uri " + uri);
        }

    }

    private int updateMovie(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgument) {
        if (values.containsKey(MovieEntry._ID)) {
            int movieId = values.getAsInteger(MovieEntry._ID);
            if (movieId < 0) {
                throw new IllegalArgumentException("Invalid movie id");
            }
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsAffected = db.update(MovieEntry.TABLE_NAME, values, selection, selectionArgument);

        if (rowsAffected != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsAffected;
    }

}
