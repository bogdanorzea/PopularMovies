package com.bogdanorzea.popularmovies

import com.bogdanorzea.popularmovies.model.`object`.Genre
import com.bogdanorzea.popularmovies.model.`object`.Movie
import com.bogdanorzea.popularmovies.utility.DataUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class ObjectUtilsTest {
    @Test
    @Throws(Exception::class)
    fun formatMoney() {
        assertEquals("$100", DataUtils.formatMoney(100))
        assertEquals("$1,000", DataUtils.formatMoney(1000))
        assertEquals("$987,483,777", DataUtils.formatMoney(987483777))
    }

    @Test
    @Throws(Exception::class)
    fun formatDuration() {
        assertEquals("40m", DataUtils.formatDuration(40))
        assertEquals("1h 0m", DataUtils.formatDuration(60))
    }

    @Test
    @Throws(Exception::class)
    fun formatYear() {
        val movie = Movie()
        movie.releaseDate = "1999-10-12"
        assertEquals("1999", movie.getYear())
    }

    @Test
    @Throws(Exception::class)
    fun printGenres() {
        val movie = Movie()
        assertEquals("Unknown", movie.printGenres())
        val g1 = Genre("First")
        movie.genres = listOf(g1)
        assertEquals("First", movie.printGenres())
        val g2 = Genre("Second")
        movie.genres = listOf(g1, g2)
        assertEquals("First, Second", movie.printGenres())
    }

    @Test
    @Throws(Exception::class)
    fun quoteString() {
        assertEquals("", DataUtils.quoteString(""))
        assertEquals("\"a\"", DataUtils.quoteString("a"))
    }
}
