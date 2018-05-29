package com.bogdanorzea.popularmovies.data

import android.content.ContentValues
import android.database.Cursor
import com.bogdanorzea.popularmovies.model.`object`.Genre
import com.bogdanorzea.popularmovies.model.`object`.Movie

fun Cursor.toMovie(): Movie {
    val idColumnIndex = getColumnIndex(MoviesContract.MovieEntry._ID)
    val titleColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_TITLE)
    val favoriteColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE)
    val releaseDateColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE)
    val taglineColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_TAGLINE)
    val overviewColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW)
    val gendersColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_GENDERS)
    val runtimeColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_RUNTIME)
    val popularityColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY)
    val voteAverageColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE)
    val voteCountColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_COUNT)
    val budgetColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_BUDGET)
    val revenueColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_REVENUE)
    val homepageColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_HOMEPAGE)
    val backdropPathColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH)
    val posterPathColumnIndex = getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH)

    return Movie().apply {
        id = getInt(idColumnIndex)
        title = getString(titleColumnIndex)
        favorite = getInt(favoriteColumnIndex)
        releaseDate = getString(releaseDateColumnIndex)
        tagline = getString(taglineColumnIndex)
        overview = getString(overviewColumnIndex)
        runtime = getInt(runtimeColumnIndex)
        popularity = getDouble(popularityColumnIndex)
        voteAverage = getDouble(voteAverageColumnIndex)
        voteCount = getInt(voteCountColumnIndex)
        budget = getInt(budgetColumnIndex)
        revenue = getLong(revenueColumnIndex)
        homepage = getString(homepageColumnIndex)
        backdropPath = getString(backdropPathColumnIndex)
        posterPath = getString(posterPathColumnIndex)
        genres = getString(gendersColumnIndex)
                .split(",".toRegex())
                .map { Genre(it.trim()) }
    }
}

fun Movie.toContentValues(): ContentValues {
    return ContentValues().apply {
        put(MoviesContract.MovieEntry._ID, id)
        put(MoviesContract.MovieEntry.COLUMN_NAME_TITLE, title)
        put(MoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, releaseDate)
        put(MoviesContract.MovieEntry.COLUMN_NAME_TAGLINE, tagline)
        put(MoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW, overview)
        put(MoviesContract.MovieEntry.COLUMN_NAME_GENDERS, printGenres())
        put(MoviesContract.MovieEntry.COLUMN_NAME_RUNTIME, runtime)
        put(MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY, popularity)
        put(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, voteAverage)
        put(MoviesContract.MovieEntry.COLUMN_NAME_VOTE_COUNT, voteCount)
        put(MoviesContract.MovieEntry.COLUMN_NAME_BUDGET, budget)
        put(MoviesContract.MovieEntry.COLUMN_NAME_REVENUE, revenue)
        put(MoviesContract.MovieEntry.COLUMN_NAME_HOMEPAGE, homepage)
        put(MoviesContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, backdropPath)
        put(MoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH, posterPath)
    }
}
