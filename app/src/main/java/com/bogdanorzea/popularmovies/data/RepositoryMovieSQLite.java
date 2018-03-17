package com.bogdanorzea.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bogdanorzea.popularmovies.model.object.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RepositoryMovieSQLite implements RepositoryMovie<Movie> {
    private final Mapper<Cursor, Movie> toMovieMapper;
    private final Mapper<Movie, ContentValues> toContentValuesMapper;
    private final Context context;

    private void storeMovieImages(@NonNull Movie movie) {
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movie.id));
        storeImage(movieUri, movie.getBackdropUrl(), MoviesContract.MovieEntry.COLUMN_NAME_BACKDROP_IMAGE);
        storeImage(movieUri, movie.getPosterUrl(), MoviesContract.MovieEntry.COLUMN_NAME_POSTER_IMAGE);
    }

    private void storeImage(Uri movieUri, String imageUrl, final String destinationColumnName) {
        Picasso.with(context)
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

                        ContentValues values = new ContentValues();
                        values.put(destinationColumnName, outputStream.toByteArray());

                        context.getContentResolver().update(movieUri, values, null, null);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                });
    }

    public RepositoryMovieSQLite(Context context) {
        this.context = context;

        this.toContentValuesMapper = new MapperMovieToContentValues(context);
        this.toMovieMapper = new MapperCursorToMovie();
    }

    @Override
    public void insert(@NonNull Movie movie) {
        ContentValues values = toContentValuesMapper.map(movie);
        context.getContentResolver().insert(MoviesContract.CONTENT_URI, values);

        storeMovieImages(movie);
    }

    @Override
    public void update(@NonNull Movie movie) {
        ContentValues values = toContentValuesMapper.map(movie);

        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movie.id));
        context.getContentResolver().update(movieUri, values, null, null);

        storeMovieImages(movie);
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
