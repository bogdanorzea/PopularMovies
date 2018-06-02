package com.bogdanorzea.popularmovies.ui.main

import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.data.MoviesContract
import com.bogdanorzea.popularmovies.model.`object`.Movie

import com.bogdanorzea.popularmovies.data.toMovie
import com.bogdanorzea.popularmovies.utility.getPreferredSortRule
import kotlinx.android.synthetic.main.layout_recycler_view.*

class FavoritesTab : Fragment(), LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {
    companion object {
        private const val DESC = " DESC"
        private const val LOADER_ID = 0
    }

    private lateinit var adapter: PosterAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        loaderManager.initLoader(LOADER_ID, null, this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var sortOrder: String? = null

        when (activity?.getPreferredSortRule()) {
            getString(R.string.pref_sort_by_popularity) -> sortOrder = MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY + DESC
            getString(R.string.pref_sort_by_top_rated) -> sortOrder = MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + DESC
        }

        return CursorLoader(activity!!, MoviesContract.CONTENT_URI, null, MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE + " = ?", arrayOf("1"), sortOrder)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapter = PosterAdapter(context!!)

        val moviesIds = mutableListOf<Movie>()
        data?.let {
            var i = 0
            val size = data.count
            while (i < size) {
                data.moveToPosition(i)
                moviesIds.add(data.toMovie())
                i++
            }
        }

        adapter.addMovies(moviesIds)
        recyclerView.adapter = adapter
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(context!!)
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        loaderManager.restartLoader(LOADER_ID, null, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(context!!)
                .unregisterOnSharedPreferenceChangeListener(this)
    }
}
