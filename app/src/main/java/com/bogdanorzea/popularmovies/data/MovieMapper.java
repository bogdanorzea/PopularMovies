package com.bogdanorzea.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.bogdanorzea.popularmovies.model.object.Movie;

public abstract class MovieMapper {

    public static Movie fromCursor(Cursor cursor) {
        Movie movie = new Movie();

        int idColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry._ID);
        int titleColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_TITLE);
        int releaseDateColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE);
        int taglineColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_TAGLINE);
        int overviewColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW);
        int runtimeColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_RUNTIME);
        int popularityColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY);
        int voteAverageColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE);
        int voteCountColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_COUNT);
        int budgetColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_BUDGET);
        int revenueColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_REVENUE);
        int homepageColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_HOMEPAGE);
        int backdropPathColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH);
        int posterPathColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH);
        int backdropImageColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_BACKDROP_IMAGE);
        int posterImageColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_POSTER_IMAGE);

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
        movie.backdropImage = cursor.getBlob(backdropImageColumnIndex);
        movie.posterImage = cursor.getBlob(posterImageColumnIndex);

        return movie;
    }

    public static ContentValues toContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry._ID, movie.id);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_TITLE, movie.title);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.releaseDate);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_TAGLINE, movie.tagline);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.overview);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_RUNTIME, movie.runtime);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY, movie.popularity);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.voteAverage);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_COUNT, movie.voteCount);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_BUDGET, movie.budget);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_REVENUE, movie.revenue);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_HOMEPAGE, movie.homepage);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.backdropPath);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movie.posterPath);

        return values;
    }
}
