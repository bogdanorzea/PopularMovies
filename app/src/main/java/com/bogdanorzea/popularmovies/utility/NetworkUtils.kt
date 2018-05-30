package com.bogdanorzea.popularmovies.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.bogdanorzea.popularmovies.BuildConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object NetworkUtils {
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val POSTER_SIZE = "w500"
    const val BACKDROP_SIZE = "w500"
    const val PROFILE_SIZE = "w500"

    private const val SEARCH = "search"
    private const val QUERY = "query"
    private const val VIDEOS = "videos"
    private const val PAGE = "page"
    private const val HTTPS = "https"
    private const val HOST = "api.themoviedb.org"
    private const val API_VERSION = "3"
    private const val MOVIE = "movie"
    private const val API_KEY = "api_key"
    private const val POPULAR = "popular"
    private const val TOP_RATED = "top_rated"
    private const val REVIEWS = "reviews"
    private const val CREDITS = "credits"


    /**
     * Returns the response string from the HttpUrl address
     *
     * @param url
     * @return
     * @throws IOException
     */
    @JvmStatic
    @Throws(IOException::class)
    fun fetch(url: HttpUrl): String {
        val client = OkHttpClient()

        val request: Request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request)
                .execute()
                .use { response -> return response.body()?.string() ?: "" }
    }

    /**
     * Builds the HttpUrl for the movie details
     *
     * @param movieId Movie ID
     * @return
     */
    @JvmStatic
    fun movieDetailsUrl(movieId: Int): HttpUrl {
        return HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(movieId.toString())
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build()
    }

    /**
     * Builds the HttpUrl for the most popular movies list
     *
     * @return
     */
    @JvmStatic
    fun moviePopularUrl(pageNumber: Int): HttpUrl {
        return HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(POPULAR)
                .addQueryParameter(PAGE, pageNumber.toString())
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build()
    }

    /**
     * Builds the HttpUrl for the top rated movies list
     *
     * @return
     */
    @JvmStatic
    fun movieTopRatedUrl(pageNumber: Int): HttpUrl {
        return HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(TOP_RATED)
                .addQueryParameter(PAGE, pageNumber.toString())
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build()
    }

    /**
     * Builds the HttpUrl for the movie videos
     *
     * @param movieId
     * @return
     */
    @JvmStatic
    fun movieVideosUrl(movieId: Int): HttpUrl {
        return HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(movieId.toString())
                .addPathSegment(VIDEOS)
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build()
    }

    /**
     * Builds the HttpUrl for the movie review list
     *
     * @param movieId
     * @param pageNumber
     * @return
     */
    @JvmStatic
    fun movieReviewsUrl(movieId: Int, pageNumber: Int): HttpUrl {
        return HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(movieId.toString())
                .addPathSegment(REVIEWS)
                .addQueryParameter(PAGE, pageNumber.toString())
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build()
    }

    /**
     * Builds the HttpUrl for the movie credit list
     *
     * @param movieId
     * @return
     */
    @JvmStatic
    fun movieCreditsUrl(movieId: Int): HttpUrl {
        return HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(movieId.toString())
                .addPathSegment(CREDITS)
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build()
    }

    @JvmStatic
    fun movieSearchUrl(queryString: String, pageNumber: Int): HttpUrl {
        return HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(SEARCH)
                .addPathSegment(MOVIE)
                .addQueryParameter(QUERY, queryString)
                .addQueryParameter(PAGE, pageNumber.toString())
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build()
    }

}

/**
 * Checks if there is an active internet connection
 */
fun Context.hasInternetConnection(): Boolean {
    val cm: ConnectivityManager = this.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo = cm.activeNetworkInfo

    return activeNetwork.isConnectedOrConnecting
}
