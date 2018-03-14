package com.bogdanorzea.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import static com.bogdanorzea.popularmovies.data.MoviesContract.MovieEntry;
import static com.bogdanorzea.popularmovies.utility.NetworkUtils.posterFullPath;


public class MovieSQLiteRepository implements MovieRepository<Movie> {
    private final Mapper<Cursor, Movie> toMovieMapper;
    private final Mapper<Movie, ContentValues> toContentValuesMapper;


    private final Context context;

    public MovieSQLiteRepository(Context context) {
        this.context = context;

        this.toContentValuesMapper = new MovieToContentValuesMapper(context);
        this.toMovieMapper = new CursorToMovieMapper();
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
        values.put(MovieEntry._ID, movieId);
        values.put(MovieEntry.COLUMN_NAME_FAVORITE, isFavorite ? 1 : 0);

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
                int favoriteColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_FAVORITE);
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
            cursor = context.getContentResolver().query(MoviesContract.CONTENT_URI, null, MovieEntry.COLUMN_NAME_FAVORITE + " = ?", new String[]{"1"}, sortOrder);
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

    private static class CursorToMovieMapper implements Mapper<Cursor, Movie> {
        @Override
        public Movie map(Cursor cursor) {
            Movie movie = new Movie();

            int idColumnIndex = cursor.getColumnIndex(MovieEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE);
            int releaseDateColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_DATE);
            int taglineColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TAGLINE);
            int overviewColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW);
            int runtimeColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RUNTIME);
            int popularityColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POPULARITY);
            int voteAverageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_AVERAGE);
            int voteCountColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_COUNT);
            int budgetColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_BUDGET);
            int revenueColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_REVENUE);
            int homepageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_HOMEPAGE);
            int backdropPathColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_BACKDROP_PATH);
            int posterPathColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_PATH);
            int posterImageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_IMAGE);

            movie.id = cursor.getInt(idColumnIndex);
            movie.title = cursor.getString(titleColumnIndex);
            movie.releaseDate = cursor.getString(releaseDateColumnIndex);
            movie.tagline = cursor.getString(taglineColumnIndex);
            movie.overview = cursor.getString(overviewColumnIndex);
            movie.runtime = cursor.getInt(runtimeColumnIndex);
            movie.popularity = cursor.getDouble(popularityColumnIndex);
            movie.voteAverage = cursor.getDouble(voteAverageColumnIndex);
            movie.voteCount = cursor.getInt(voteCountColumnIndex);
            movie.budget = cursor.getInt(budgetColumnIndex);
            movie.revenue = cursor.getLong(revenueColumnIndex);
            movie.homepage = cursor.getString(homepageColumnIndex);
            movie.backdropPath = cursor.getString(backdropPathColumnIndex);
            movie.posterPath = cursor.getString(posterPathColumnIndex);
            movie.image = cursor.getBlob(posterImageColumnIndex);

            return movie;
        }
    }

    private static class MovieToContentValuesMapper implements Mapper<Movie, ContentValues> {
        private final Context context;

        MovieToContentValuesMapper(Context context) {
            this.context = context;
        }

        @Override
        public ContentValues map(@NonNull Movie movie) {
            ContentValues values = new ContentValues();
            values.put(MovieEntry._ID, movie.id);
            values.put(MovieEntry.COLUMN_NAME_TITLE, movie.title);
            values.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.releaseDate);
            values.put(MovieEntry.COLUMN_NAME_TAGLINE, movie.tagline);
            values.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.overview);
            values.put(MovieEntry.COLUMN_NAME_RUNTIME, movie.runtime);
            values.put(MovieEntry.COLUMN_NAME_POPULARITY, movie.popularity);
            values.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.voteAverage);
            values.put(MovieEntry.COLUMN_NAME_VOTE_COUNT, movie.voteCount);
            values.put(MovieEntry.COLUMN_NAME_BUDGET, movie.budget);
            values.put(MovieEntry.COLUMN_NAME_REVENUE, movie.revenue);
            values.put(MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.backdropPath);
            values.put(MovieEntry.COLUMN_NAME_POSTER_PATH, movie.posterPath);
            values.put(MovieEntry.COLUMN_NAME_POSTER_IMAGE,
                    NetworkUtils.getImageBytes(context, posterFullPath(movie.posterPath)));

            return values;
        }
    }
}
