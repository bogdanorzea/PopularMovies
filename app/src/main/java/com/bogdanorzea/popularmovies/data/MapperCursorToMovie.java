package com.bogdanorzea.popularmovies.data;

import android.database.Cursor;

import com.bogdanorzea.popularmovies.model.object.Movie;

class MapperCursorToMovie implements Mapper<Cursor, Movie> {
    @Override
    public Movie map(Cursor cursor) {
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
}
