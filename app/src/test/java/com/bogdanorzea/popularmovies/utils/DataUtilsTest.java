package com.bogdanorzea.popularmovies.utils;

import com.bogdanorzea.popularmovies.model.objects.Genre;

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
        assertEquals(DataUtils.formatDuration(40), "40m");
        assertEquals(DataUtils.formatDuration(60), "1h 0m");
    }

    @Test
    public void printGenres() throws Exception {
        List<Genre> genreList = new ArrayList<>();
        assertEquals(DataUtils.printGenres(genreList), "Unknown");
        Genre g = new Genre();
        g.name = "First";
        genreList.add(g);
        assertEquals(DataUtils.printGenres(genreList), "First");
        Genre g2 = new Genre();
        g2.name = "Second";
        genreList.add(g2);
        assertEquals(DataUtils.printGenres(genreList), "First, Second");
    }

    @Test
    public void quoteString() throws Exception {
        assertEquals(DataUtils.quoteString(""), "");
        assertEquals(DataUtils.quoteString("a"), "\"a\"");
    }
}