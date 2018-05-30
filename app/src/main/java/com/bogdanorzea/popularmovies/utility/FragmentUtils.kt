package com.bogdanorzea.popularmovies.utility

import android.os.Bundle
import android.support.v4.app.Fragment


/**
 * Builds a fragment and passes as arguments the movie id
 * @param movieId
 * @param fragmentClass
 * @return
 */
fun buildFragment(movieId: Int, fragmentClass: Class<out Fragment>): Fragment {
    var fragment: Fragment? = null

    try {
        fragment = fragmentClass.newInstance()
    } catch (e: InstantiationException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }

    if (fragment == null)
        throw RuntimeException("Could not instantiate fragment class")

    val bundle = Bundle()
    bundle.putInt("movie_id", movieId)
    fragment.arguments = bundle

    return fragment
}
