package com.bogdanorzea.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.bogdanorzea.popularmovies.model.object.Genre;
import com.bogdanorzea.popularmovies.model.object.Movie;

import java.util.ArrayList;

public abstract class MovieMapper {

    public static Movie fromCursor(Cursor cursor) {
        Movie movie = new Movie();

        int idColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry._ID);
        int titleColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_TITLE);
        int favoriteColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE);
        int releaseDateColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE);
        int taglineColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_TAGLINE);
        int overviewColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW);
        int gendersColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_GENDERS);
        int runtimeColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_RUNTIME);
        int popularityColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY);
        int voteAverageColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE);
        int voteCountColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_COUNT);
        int budgetColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_BUDGET);
        int revenueColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_REVENUE);
        int homepageColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_HOMEPAGE);
        int backdropPathColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH);
        int posterPathColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH);

        movie.setId(cursor.getInt(idColumnIndex));
        movie.setTitle(cursor.getString(titleColumnIndex));
        movie.setFavorite(cursor.getInt(favoriteColumnIndex));
        movie.setReleaseDate(cursor.getString(releaseDateColumnIndex));
        movie.setTagline(cursor.getString(taglineColumnIndex));
        movie.setOverview(cursor.getString(overviewColumnIndex));
        movie.setRuntime(cursor.getInt(runtimeColumnIndex));
        movie.setPopularity(cursor.getDouble(popularityColumnIndex));
        movie.setVoteAverage(cursor.getDouble(voteAverageColumnIndex));
        movie.setVoteCount(cursor.getInt(voteCountColumnIndex));
        movie.setBudget(cursor.getInt(budgetColumnIndex));
        movie.setRevenue(cursor.getLong(revenueColumnIndex));
        movie.setHomepage(cursor.getString(homepageColumnIndex));
        movie.setBackdropPath(cursor.getString(backdropPathColumnIndex));
        movie.setPosterPath(cursor.getString(posterPathColumnIndex));

        String genderStrings = cursor.getString(gendersColumnIndex);
        if (!TextUtils.isEmpty(genderStrings)) {
            movie.setGenres(new ArrayList<>());
            if (genderStrings.contains(",")) {
                String[] items = genderStrings.split(",");

                for (String item : items) {
                    movie.getGenres().add(new Genre(item));
                }
            } else {
                movie.getGenres().add(new Genre(genderStrings));
            }
        }

        return movie;
    }

    public static ContentValues toContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry._ID, movie.getId());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_TAGLINE, movie.getTagline());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_GENDERS, movie.printGenres());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_RUNTIME, movie.getRuntime());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY, movie.getPopularity());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_COUNT, movie.getVoteCount());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_BUDGET, movie.getBudget());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_REVENUE, movie.getRevenue());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_HOMEPAGE, movie.getHomepage());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getPosterPath());

        return values;
    }
}
