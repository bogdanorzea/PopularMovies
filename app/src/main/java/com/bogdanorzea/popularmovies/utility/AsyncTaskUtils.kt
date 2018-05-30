package com.bogdanorzea.popularmovies.utility


import android.os.AsyncTask
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import java.io.IOException

abstract class AsyncTaskUtils {

    /**
     * Interface separation between AsyncTask and Activities
     *
     * @param <T> Response type
     */
    interface RequestTaskListener<T> {
        fun onTaskStarting()

        fun onTaskComplete(result: T?)
    }

    /**
     * Generic AsyncTask that makes network requests to theMovieDB API and parses the response
     *
     * @param <T> Response type
     */
    class RequestTask<T>(private val listener: RequestTaskListener<T>, private val classType: Class<T>) : AsyncTask<HttpUrl, Void, T>() {

        override fun onPreExecute() {
            super.onPreExecute()
            listener.onTaskStarting()
        }

        override fun doInBackground(vararg httpUrls: HttpUrl): T? {
            val stringResponse: String = try {
                NetworkUtils.fetch(httpUrls[0])
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }

            val moshi: Moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            val jsonAdapter = moshi.adapter(classType)

            return try {
                jsonAdapter.fromJson(stringResponse)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(response: T) {
            super.onPostExecute(response)

            listener.onTaskComplete(response)
        }
    }

}
