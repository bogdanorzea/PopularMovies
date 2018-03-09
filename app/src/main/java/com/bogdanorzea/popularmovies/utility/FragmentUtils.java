package com.bogdanorzea.popularmovies.utility;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;


public class FragmentUtils {
    /**
     * Builds a fragment and passes as arguments the parcelable movie object
     * @param movie
     * @param fragmentClass
     * @return
     */
    public static Fragment buildFragment(Parcelable movie, Class<? extends Fragment> fragmentClass) {
        Fragment fragment = null;

        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Builds a fragment and passes as arguments the movie id
     * @param movieId
     * @param fragmentClass
     * @return
     */
    public static Fragment buildFragment(int movieId, Class<? extends Fragment> fragmentClass) {
        Fragment fragment = null;

        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("movie_id", movieId);
        fragment.setArguments(bundle);

        return fragment;
    }
}
