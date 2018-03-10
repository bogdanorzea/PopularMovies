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

    private Cursor queryFavorites(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return insertMovie(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for Uri " + uri);
        }
    }

    private Uri insertMovie(Uri uri, ContentValues contentValues) {
        Integer movieId = contentValues.getAsInteger(MovieEntry._ID);
        if (movieId < 0) {
            throw new IllegalArgumentException("Invalid movie id");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = db.insert(MovieEntry.TABLE_NAME, null, contentValues);

        if (newRowId < 0) {
            Timber.e("Failed to insert %s for Uri %s", movieId, uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
