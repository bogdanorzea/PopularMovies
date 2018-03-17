package com.bogdanorzea.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bogdanorzea.popularmovies.model.object.Movie;

class MapperMovieToContentValues implements Mapper<Movie, ContentValues> {
    private final Context context;

    MapperMovieToContentValues(Context context) {
        this.context = context;
    }

    @Override
    public ContentValues map(@NonNull Movie movie) {
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
