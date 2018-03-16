package com.bogdanorzea.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bogdanorzea.popularmovies.model.object.Movie;

import java.util.ArrayList;
import java.util.List;

public class RepositoryMovieSQLite implements RepositoryMovie<Movie> {
    private final Mapper<Cursor, Movie> toMovieMapper;
    private final Mapper<Movie, ContentValues> toContentValuesMapper;


    private final Context context;

    public RepositoryMovieSQLite(Context context) {
        this.context = context;

        this.toContentValuesMapper = new MapperMovieToContentValues(context);
        this.toMovieMapper = new MapperCursorToMovie();
    }

    @Override
    public void insert(@NonNull Movie movie) {
        context.getContentResolver().insert(MoviesContract.CONTENT_URI, toContentValuesMapper.map(movie));
    }

    @Override
    public void update(@NonNull Movie movie) {
        ContentValues values = toContentValuesMapper.map(movie);

        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movie.id));
        context.getContentResolver().update(movieUri, values, null, null);
    }

    @Override
    public void setFavorite(int movieId, boolean isFavorite) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry._ID, movieId);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE, isFavorite ? 1 : 0);

        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));
        context.getContentResolver().update(movieUri, values, null, null);
    }

    @Override
    public boolean isFavorite(int movieId) {
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));
        boolean isFavorite = false;

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(movieUri, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int favoriteColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE);
                int favoriteValue = cursor.getInt(favoriteColumnIndex);

                if (favoriteValue == 1) {
                    isFavorite = true;
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return isFavorite;
    }

    @Override
    public void delete(int movieId) {
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));
        context.getContentResolver().delete(movieUri, null, null);
    }

    @Override
    public void deleteAll() {
        context.getContentResolver().delete(MoviesContract.CONTENT_URI, null, null);
    }

    @Override
    public List<Movie> getAll() {
        final List<Movie> movies = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MoviesContract.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                for (int i = 0, size = cursor.getCount(); i < size; i++) {
                    cursor.moveToPosition(i);

                    movies.add(toMovieMapper.map(cursor));
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return movies;
    }

    @Override
    public List<Movie> getFavorites(String sortOrder) {
        final List<Movie> movies = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MoviesContract.CONTENT_URI, null, MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE + " = ?", new String[]{"1"}, sortOrder);
            if (cursor != null) {
                for (int i = 0, size = cursor.getCount(); i < size; i++) {
                    cursor.moveToPosition(i);

                    movies.add(toMovieMapper.map(cursor));
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return movies;
    }

    @Override
    public Movie get(int movieId) {
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));

        Movie movie = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(movieUri, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                movie = toMovieMapper.map(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return movie;
    }

}
