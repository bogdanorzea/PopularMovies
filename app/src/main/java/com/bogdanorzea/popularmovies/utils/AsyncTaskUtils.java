package com.bogdanorzea.popularmovies.utils;


import android.os.AsyncTask;

import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.model.response.MoviesResponse;
import com.bogdanorzea.popularmovies.model.response.ReviewsResponse;
import com.bogdanorzea.popularmovies.model.response.VideosResponse;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.HttpUrl;

public class AsyncTaskUtils {
    public interface AsyncTaskListener<T> {
        void onTaskStarting();

        void onTaskComplete(T result);
    }

    public static class ListOfMoviesAsyncTask extends AsyncTask<HttpUrl, Void, MoviesResponse> {
        private final AsyncTaskListener<MoviesResponse> listener;

        public ListOfMoviesAsyncTask(AsyncTaskListener<MoviesResponse> listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onTaskStarting();
        }

        @Override
        protected MoviesResponse doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<MoviesResponse> jsonAdapter = moshi.adapter(MoviesResponse.class);

            MoviesResponse moviesResponse = null;

            try {
                moviesResponse = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return moviesResponse;
        }

        @Override
        protected void onPostExecute(MoviesResponse moviesResponse) {
            super.onPostExecute(moviesResponse);
            listener.onTaskComplete(moviesResponse);
        }
    }

    public static class MovieDetailsAsyncTask extends AsyncTask<HttpUrl, Void, Movie> {
        private final AsyncTaskUtils.AsyncTaskListener<Movie> listener;

        public MovieDetailsAsyncTask(AsyncTaskUtils.AsyncTaskListener<Movie> listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onTaskStarting();
        }

        @Override
        protected Movie doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Movie> jsonAdapter = moshi.adapter(Movie.class);

            Movie movie = null;

            try {
                movie = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            listener.onTaskComplete(movie);
        }
    }

    public static class MovieVideosAsyncTask extends AsyncTask<HttpUrl, Void, VideosResponse> {
        private final AsyncTaskListener<VideosResponse> listener;

        public MovieVideosAsyncTask(AsyncTaskUtils.AsyncTaskListener<VideosResponse> listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onTaskStarting();
        }

        @Override
        protected VideosResponse doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<VideosResponse> jsonAdapter = moshi.adapter(VideosResponse.class);

            VideosResponse videosResponse = null;

            try {
                videosResponse = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return videosResponse;
        }

        @Override
        protected void onPostExecute(VideosResponse videosResponse) {
            super.onPostExecute(videosResponse);
            listener.onTaskComplete(videosResponse);
        }
    }

    public static class MovieReviewsAsyncTask extends AsyncTask<HttpUrl, Void, ReviewsResponse> {
        private final AsyncTaskListener<ReviewsResponse> listener;

        public MovieReviewsAsyncTask(AsyncTaskUtils.AsyncTaskListener<ReviewsResponse> listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onTaskStarting();
        }

        @Override
        protected ReviewsResponse doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<ReviewsResponse> jsonAdapter = moshi.adapter(ReviewsResponse.class);

            ReviewsResponse videosResponse = null;

            try {
                videosResponse = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return videosResponse;
        }

        @Override
        protected void onPostExecute(ReviewsResponse reviewsResponse) {
            super.onPostExecute(reviewsResponse);
            listener.onTaskComplete(reviewsResponse);
        }
    }

}
