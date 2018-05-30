package com.bogdanorzea.popularmovies

import com.bogdanorzea.popularmovies.model.`object`.Genre
import com.bogdanorzea.popularmovies.model.`object`.Movie
import com.bogdanorzea.popularmovies.utility.addQuotes
import com.bogdanorzea.popularmovies.utility.toMoneyString
import com.bogdanorzea.popularmovies.utility.toTimeString
import org.junit.Assert.assertEquals
import org.junit.Test

class ObjectUtilsTest {
    @Test
    @Throws(Exception::class)
    fun formatMoney() {
        assertEquals("$100", 100L.toMoneyString())
        assertEquals("$1,000", 1000L.toMoneyString())
        assertEquals("$987,483,777", 987483777L.toMoneyString())
    }

    @Test
    @Throws(Exception::class)
    fun formatDuration() {
        assertEquals("40m", 40.toTimeString())
        assertEquals("1h 0m", 60.toTimeString())
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
        assertEquals("", "".addQuotes())
        assertEquals("\"a\"", "a".addQuotes())
    }
}
