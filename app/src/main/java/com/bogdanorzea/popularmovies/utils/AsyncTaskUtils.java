package com.bogdanorzea.popularmovies.utils;


import android.os.AsyncTask;

import com.bogdanorzea.popularmovies.model.response.ListOfMoviesResponse;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.HttpUrl;

public class AsyncTaskUtils {
    public interface AsyncTaskCompleteListener<T> {
        void onTaskStarting();
        void onTaskComplete(T result);
    }

    public static class ListOfMoviesAsyncTask extends AsyncTask<HttpUrl, Void, ListOfMoviesResponse> {
        private final AsyncTaskUtils.AsyncTaskCompleteListener<ListOfMoviesResponse> listener;

        public ListOfMoviesAsyncTask(AsyncTaskUtils.AsyncTaskCompleteListener<ListOfMoviesResponse> listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onTaskStarting();
        }

        @Override
        protected ListOfMoviesResponse doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<ListOfMoviesResponse> jsonAdapter = moshi.adapter(ListOfMoviesResponse.class);

            ListOfMoviesResponse listOfMoviesResponse = null;

            try {
                listOfMoviesResponse = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return listOfMoviesResponse;
        }

        @Override
        protected void onPostExecute(ListOfMoviesResponse listOfMoviesResponse) {
            super.onPostExecute(listOfMoviesResponse);
            listener.onTaskComplete(listOfMoviesResponse);
        }
    }

}
