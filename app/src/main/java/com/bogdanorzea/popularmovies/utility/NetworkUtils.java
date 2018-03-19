package com.bogdanorzea.popularmovies.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.bogdanorzea.popularmovies.BuildConfig;
import com.bogdanorzea.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {
    public static final String SEARCH = "search";
    public static final String QUERY = "query";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w500";
    public static final String BACKDROP_SIZE = "w500";
    private static final String VIDEOS = "videos";
    private static final String PAGE = "page";
    private static final String HTTPS = "https";
    private static final String HOST = "api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String MOVIE = "movie";
    private static final String API_KEY = "api_key";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String REVIEWS = "reviews";
    private static final String CREDITS = "credits";

    /**
     * Returns the response string from the HttpUrl address
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String fetch(HttpUrl url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Builds the HttpUrl for the movie details
     *
     * @param movieId Movie ID
     * @return
     */
    public static HttpUrl movieDetailsUrl(int movieId) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(String.valueOf(movieId))
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build();
    }

    /**
     * Builds the HttpUrl for the most popular movies list
     *
     * @return
     */
    public static HttpUrl moviePopularUrl(int pageNumber) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(POPULAR)
                .addQueryParameter(PAGE, String.valueOf(pageNumber))
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build();
    }

    /**
     * Builds the HttpUrl for the top rated movies list
     *
     * @return
     */
    public static HttpUrl movieTopRatedUrl(int pageNumber) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(TOP_RATED)
                .addQueryParameter(PAGE, String.valueOf(pageNumber))
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build();
    }

    /**
     * Builds the HttpUrl for the movie videos
     *
     * @param movieId
     * @return
     */
    public static HttpUrl movieVideosUrl(int movieId) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(String.valueOf(movieId))
                .addPathSegment(VIDEOS)
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build();
    }

    /**
     * Builds the HttpUrl for the movie review list
     *
     * @param movieId
     * @param pageNumber
     * @return
     */
    public static HttpUrl movieReviewsUrl(int movieId, int pageNumber) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(String.valueOf(movieId))
                .addPathSegment(REVIEWS)
                .addQueryParameter(PAGE, String.valueOf(pageNumber))
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build();
    }

    /**
     * Builds the HttpUrl for the movie credit list
     *
     * @param movieId
     * @return
     */
    public static HttpUrl movieCreditsUrl(int movieId) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(MOVIE)
                .addPathSegment(String.valueOf(movieId))
                .addPathSegment(CREDITS)
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build();
    }

    /**
     * Loads the imageUrl into the ImageView
     *
     * @param context
     * @param imageView
     * @param imageUrl
     */
    public static void loadImage(Context context, ImageView imageView, String imageUrl) {
        Picasso.with(context)
                .load(imageUrl)
                .error(R.drawable.missing_cover)
                .into(imageView);
    }

    public static String posterFullPath(String relativeImagePath) {
        return IMAGE_BASE_URL + POSTER_SIZE + relativeImagePath;
    }

    /**
     * Checks if there is an active internet connection
     *
     * @param context
     * @return
     */
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    public static HttpUrl movieSearchUrl(String queryString) {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(SEARCH)
                .addPathSegment(MOVIE)
                .addQueryParameter(QUERY, queryString)
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build();
    }

}
