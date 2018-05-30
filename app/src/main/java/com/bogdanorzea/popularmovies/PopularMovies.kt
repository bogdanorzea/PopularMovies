package com.bogdanorzea.popularmovies

import android.app.Application

import timber.log.Timber

class PopularMovies : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
