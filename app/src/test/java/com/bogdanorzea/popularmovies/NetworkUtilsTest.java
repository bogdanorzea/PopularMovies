package com.bogdanorzea.popularmovies;

import com.bogdanorzea.popularmovies.utils.NetworkUtils;

import junit.framework.Assert;

import org.junit.Test;

public class NetworkUtilsTest {

    @Test
    public void getMovieUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.buildMovieUrl(550).toString(), "https://api.themoviedb.org/3/movie/550?api_key=" + BuildConfig.TheMovieDBApiKey);
    }

    @Test
    public void getDiscoverUrl() throws Exception {
        Assert.assertEquals(NetworkUtils.buildDiscoverUrl().toString(), "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + BuildConfig.TheMovieDBApiKey);
    }
}
