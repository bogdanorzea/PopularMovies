package com.bogdanorzea.popularmovies.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bogdanorzea.popularmovies.BuildConfig;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w500";
    private static final String SORT_BY = "sort_by";
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String HTTPS = "https";
    private static final String HOST = "api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String DISCOVER = "discover";
    private static final String MOVIE = "movie";
    private static final String API_KEY = "api_key";
    public static final String VIDEOS = "videos";

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
    public static HttpUrl movieDiscoverUrl() {
        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(HOST)
                .addPathSegment(API_VERSION)
                .addPathSegment(DISCOVER)
                .addPathSegment(MOVIE)
                .addQueryParameter(SORT_BY, "popularity.desc")
                .addQueryParameter(API_KEY, BuildConfig.TheMovieDBApiKey)
                .build();
    }

    /**
     * Builds the HttpUrl for the movie videos
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
     * Builds the full path from a relative image and loads it in the ImageView
     * @param context
     * @param imageView
     * @param relativeImagePath
     */
    public static void loadImage(Context context, ImageView imageView, String relativeImagePath) {
        String completeImagePath = IMAGE_BASE_URL + IMAGE_SIZE + relativeImagePath;

        Picasso.with(context).load(completeImagePath).into(imageView);
    }

}
