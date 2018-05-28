package com.bogdanorzea.popularmovies

import com.bogdanorzea.popularmovies.utility.NetworkUtils

import junit.framework.Assert

import org.junit.Test

class NetworkUtilsTest {

    @Test
    @Throws(Exception::class)
    fun movieDetailsUrl() {
        Assert.assertEquals("https://api.themoviedb.org/3/movie/550?api_key=" + BuildConfig.TheMovieDBApiKey,
                NetworkUtils.movieDetailsUrl(550).toString())
    }

    @Test
    @Throws(Exception::class)
    fun moviePopularUrl() {
        Assert.assertEquals("https://api.themoviedb.org/3/movie/popular?page=1&api_key=" + BuildConfig.TheMovieDBApiKey,
                NetworkUtils.moviePopularUrl(1).toString())
    }

    @Test
    @Throws(Exception::class)
    fun movieTopRatedUrl() {
        Assert.assertEquals("https://api.themoviedb.org/3/movie/top_rated?page=1&api_key=" + BuildConfig.TheMovieDBApiKey,
                NetworkUtils.movieTopRatedUrl(1).toString())
    }

    @Test
    @Throws(Exception::class)
    fun movieReviewsUrl() {
        Assert.assertEquals("https://api.themoviedb.org/3/movie/550/reviews?page=1&api_key=" + BuildConfig.TheMovieDBApiKey,
                NetworkUtils.movieReviewsUrl(550, 1).toString())
    }

    @Test
    @Throws(Exception::class)
    fun movieCreditsUrl() {
        Assert.assertEquals("https://api.themoviedb.org/3/movie/550/credits?api_key=" + BuildConfig.TheMovieDBApiKey,
                NetworkUtils.movieCreditsUrl(550).toString())
    }

    @Test
    @Throws(Exception::class)
    fun movieVideosUrl() {
        Assert.assertEquals("https://api.themoviedb.org/3/movie/550/videos?api_key=" + BuildConfig.TheMovieDBApiKey,
                NetworkUtils.movieVideosUrl(550).toString())
    }

    @Test
    @Throws(Exception::class)
    fun movieSearchUrl() {
        Assert.assertEquals("https://api.themoviedb.org/3/search/movie?query=Jack&page=1&api_key=" + BuildConfig.TheMovieDBApiKey,
                NetworkUtils.movieSearchUrl("Jack", 1).toString())
        Assert.assertEquals("https://api.themoviedb.org/3/search/movie?query=Jack%20Reacher&page=1&api_key=" + BuildConfig.TheMovieDBApiKey,
                NetworkUtils.movieSearchUrl("Jack Reacher", 1).toString())
    }
}
