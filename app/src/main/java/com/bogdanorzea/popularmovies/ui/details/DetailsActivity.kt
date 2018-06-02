package com.bogdanorzea.popularmovies.ui.details

import android.content.ContentValues
import android.database.Cursor
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.data.MoviesContract
import com.bogdanorzea.popularmovies.model.`object`.Movie
import com.bogdanorzea.popularmovies.ui.PagerAdapter
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils
import com.bogdanorzea.popularmovies.utility.NetworkUtils
import com.squareup.picasso.Picasso

import timber.log.Timber

import com.bogdanorzea.popularmovies.data.toContentValues
import com.bogdanorzea.popularmovies.data.toMovie
import com.bogdanorzea.popularmovies.utility.buildFragment
import com.bogdanorzea.popularmovies.utility.hasInternetConnection
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    companion object {
        const val MOVIE_ID = "movie_id"
        private const val FETCH_MOVIE_LOADER = 2
    }

    private lateinit var mMenu: Menu
    private lateinit var mMovie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appBarLayout.setExpanded(true)

        setBackdropColorFilter()

        intent?.let {
            val args = Bundle()
            val movieId = intent.getIntExtra(MOVIE_ID, -1)
            args.putInt(MOVIE_ID, movieId)
            supportLoaderManager.initLoader(FETCH_MOVIE_LOADER, args, this)

            refreshMovieInformation(movieId)
            populateTabs(movieId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        mMenu = menu

        if (::mMovie.isInitialized && mMovie.isFavorite()) {
            mMenu.changeFavoriteMenuItemResource(R.drawable.ic_favorite_white_24dp)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                toggleMovieFavoriteStatus()
                true
            }
            R.id.action_homepage -> {
                openMovieHomepage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val movieId: Int = args?.getInt(MOVIE_ID) ?: -1
        val movieUri: Uri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, movieId.toString())

        return CursorLoader(this, movieUri, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        if (data.moveToFirst()) {
            mMovie = data.toMovie()

            loadBackdropImage()
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    private fun populateTabs(movieId: Int) {
        val pagerAdapter = PagerAdapter(supportFragmentManager)
        pagerAdapter.apply {
            addFragment(buildFragment(movieId, DescriptionTab::class.java), getString(R.string.description_tab_name))
            addFragment(buildFragment(movieId, VideosTab::class.java), getString(R.string.videos_tab_name))
            addFragment(buildFragment(movieId, CastTab::class.java), getString(R.string.cast_tab_name))
            addFragment(buildFragment(movieId, ReviewsTab::class.java), getString(R.string.reviews_tab_name))
        }

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun toggleMovieFavoriteStatus() {
        if (::mMovie.isInitialized) {
            val movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, mMovie.id.toString())
            when {
                mMovie.isFavorite() -> {
                    mMenu.changeFavoriteMenuItemResource(R.drawable.ic_favorite_border_white_24dp)
                    mMovie.favorite = 0
                }
                else -> {
                    mMenu.changeFavoriteMenuItemResource(R.drawable.ic_favorite_white_24dp)
                    mMovie.favorite = 1
                }
            }

            val contentValues = ContentValues()
            contentValues.put(MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE, mMovie.favorite)

            contentResolver.update(movieUri, contentValues, null, null)
        }
    }

    private fun refreshMovieInformation(movieId: Int) {
        if (this.hasInternetConnection()) {
            val mRequestTaskListener = object : AsyncTaskUtils.RequestTaskListener<Movie> {
                override fun onTaskStarting() {}

                override fun onTaskComplete(result: Movie?) {
                    result?.let {
                        val movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, movieId.toString())

                        val contentValues = result.toContentValues()
                        val rows = contentResolver.update(movieUri, contentValues, null, null)

                        if (rows > 0)
                            Timber.d("Movie \"%s\" (%s) successfully updated", result.title, result.id)
                    }
                }
            }

            AsyncTaskUtils.RequestTask(mRequestTaskListener, Movie::class.java)
                    .execute(NetworkUtils.movieDetailsUrl(movieId))
        } else {
            Toast.makeText(this, getString(R.string.offline_limitation_warning), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBackdropColorFilter() {
        backdrop.colorFilter = PorterDuffColorFilter(resources.getColor(
                R.color.colorPrimary), PorterDuff.Mode.LIGHTEN)
    }

    private fun openMovieHomepage() {
        if (::mMovie.isInitialized && !mMovie.homepage.isNullOrEmpty()) {
            CustomTabsIntent.Builder()
                    .build()
                    .launchUrl(this@DetailsActivity, Uri.parse(mMovie.homepage))
        } else {
            Toast.makeText(this@DetailsActivity, "Couldn't launch the movie's homepage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadBackdropImage() {
        if (::mMovie.isInitialized) {
            Picasso.with(this)
                    .load(mMovie.getBackdropUrl())
                    .into(backdrop)
        }
    }
}

fun Menu.changeFavoriteMenuItemResource(imageResource: Int) {
    val menuItemFavorite = this.findItem(R.id.action_favorite)
    menuItemFavorite.setIcon(imageResource)
}
