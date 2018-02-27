package com.bogdanorzea.popularmovies;

import com.bogdanorzea.popularmovies.utils.NetworkUtils;

import junit.framework.Assert;

import org.junit.Test;

public class NetworkUtilsTest {

    @Test
    public void movieDetailsUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.movieDetailsUrl(550).toString(), "https://api.themoviedb.org/3/movie/550?api_key=" + BuildConfig.TheMovieDBApiKey);
    }

    @Test
    public void movieDiscoverUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.movieDiscoverUrl(1, "popularity.desc").toString(), "https://api.themoviedb.org/3/discover/movie?page=1&sort_by=popularity.desc&api_key=" + BuildConfig.TheMovieDBApiKey);
    }

    @Test
    public void movieVideosUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.movieVideosUrl(550).toString(), "https://api.themoviedb.org/3/movie/550/videos?api_key=" + BuildConfig.TheMovieDBApiKey);
    }
}
