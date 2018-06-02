package com.bogdanorzea.popularmovies.ui.details

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.data.MoviesContract
import com.squareup.picasso.Picasso

import com.bogdanorzea.popularmovies.data.toMovie
import com.bogdanorzea.popularmovies.utility.addParenthesis
import com.bogdanorzea.popularmovies.utility.addQuotes
import com.bogdanorzea.popularmovies.utility.toMoneyString
import com.bogdanorzea.popularmovies.utility.toTimeString
import kotlinx.android.synthetic.main.layout_description.*


class DescriptionTab : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    companion object {
        private const val LOADER_ID = 2
    }

    private var movieId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ViewCompat.setNestedScrollingEnabled(scrollView, true)

        movieId = arguments?.getInt("movie_id") ?: -1
    }

    private fun displayDescription(cursor: Cursor) {
        view ?: return

        if (cursor.moveToFirst()) {
            val movie = cursor.toMovie()

            Picasso.with(context)
                    .load(movie.getPosterUrl())
                    .error(R.drawable.missing_cover)
                    .into(poster)
            releaseDate.text = movie.getYear().addParenthesis()
            title.text = movie.title
            tagline.text = movie.tagline?.addQuotes()
            overview.text = movie.overview
            score.rating = movie.voteAverage.toFloat() / 2
            runtime.text = movie.runtime?.toTimeString()
            budget.text = movie.budget.toLong().toMoneyString()
            revenue.text = movie.revenue.toMoneyString()
            genre.text = movie.printGenres()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        loaderManager.initLoader(LOADER_ID, null, this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, movieId.toString())

        return CursorLoader(activity!!, movieUri, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        displayDescription(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}
