package com.bogdanorzea.popularmovies.utility;

import com.bogdanorzea.popularmovies.model.object.Genre;
import com.bogdanorzea.popularmovies.model.object.Movie;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataUtilsTest {
    @Test
    public void formatMoney() throws Exception {
        assertEquals(DataUtils.formatMoney(100), "$100");
        assertEquals(DataUtils.formatMoney(1000), "$1,000");
        assertEquals(DataUtils.formatMoney(987483777), "$987,483,777");
    }

    @Test
    public void formatDuration() throws Exception {
        assertEquals(DataUtils.formatDuration(40), "40m");
        assertEquals(DataUtils.formatDuration(60), "1h 0m");
    }

    @Test
    public void formatYear() throws Exception {
        Movie movie = new Movie();
        movie.releaseDate = "1999-10-12";
        assertEquals(movie.getYear(), "1999");
    }

    @Test
    public void printGenres() throws Exception {
        Movie movie = new Movie();
        assertEquals(movie.printGenres(), "Unknown");
        Genre g = new Genre();
        g.name = "First";
        movie.genres = new ArrayList<>();
        movie.genres.add(g);
        assertEquals(movie.printGenres(), "First");
        Genre g2 = new Genre();
        g2.name = "Second";
        movie.genres.add(g2);
        assertEquals(movie.printGenres(), "First, Second");
    }

    @Test
    public void quoteString() throws Exception {
        assertEquals(DataUtils.quoteString(""), "");
        assertEquals(DataUtils.quoteString("a"), "\"a\"");
    }
}