package com.bogdanorzea.popularmovies;

import android.app.Application;

import timber.log.Timber;

public class PopularMovies extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
