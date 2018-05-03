package com.bogdanorzea.popularmovies.utility;


import android.os.AsyncTask;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.HttpUrl;

public abstract class AsyncTaskUtils {

    /**
     * Interface separation between AsyncTask and Activities
     *
     * @param <T> Response type
     */
    public interface RequestTaskListener<T> {
        void onTaskStarting();

        void onTaskComplete(T result);
    }

    /**
     * Generic AsyncTask that makes network requests to theMovieDB API and parse the response
     *
     * @param <T> Response type
     */
    public static class RequestTask<T> extends AsyncTask<HttpUrl, Void, T> {
        private final RequestTaskListener<T> listener;
        private final Class<T> classType;

        public RequestTask(RequestTaskListener<T> listener, Class<T> classType) {
            this.listener = listener;
            this.classType = classType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onTaskStarting();
        }

        @Override
        protected T doInBackground(HttpUrl... httpUrls) {
            String stringResponse = "";
            try {
                stringResponse = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder()
                    .add(FallbackOnNull.ADAPTER_FACTORY)
                    .build();
            JsonAdapter<T> jsonAdapter = moshi.adapter(classType);

            T jsonResponse = null;

            try {
                jsonResponse = jsonAdapter.fromJson(stringResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(T response) {
            super.onPostExecute(response);

            listener.onTaskComplete(response);
        }
    }

}
