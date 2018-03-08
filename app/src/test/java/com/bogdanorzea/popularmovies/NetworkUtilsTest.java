package com.bogdanorzea.popularmovies;

import com.bogdanorzea.popularmovies.utility.NetworkUtils;

import junit.framework.Assert;

import org.junit.Test;

public class NetworkUtilsTest {

    @Test
    public void movieDetailsUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.movieDetailsUrl(550).toString(), "https://api.themoviedb.org/3/movie/550?api_key=" + BuildConfig.TheMovieDBApiKey);
    }

    @Test
    public void moviePopularUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.moviePopularUrl(1).toString(), "https://api.themoviedb.org/3/movie/popular?page=1&api_key=" + BuildConfig.TheMovieDBApiKey);
    }

    @Test
    public void movieTopRatedUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.movieTopRatedUrl(1).toString(), "https://api.themoviedb.org/3/movie/top_rated?page=1&api_key=" + BuildConfig.TheMovieDBApiKey);
    }

    @Test
    public void movieReviewsUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.movieReviewsUrl(550, 1).toString(), "https://api.themoviedb.org/3/movie/550/reviews?page=1&api_key=" + BuildConfig.TheMovieDBApiKey);
    }

    @Test
    public void movieVideosUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.movieVideosUrl(550).toString(), "https://api.themoviedb.org/3/movie/550/videos?api_key=" + BuildConfig.TheMovieDBApiKey);
    }
}
