package com.bogdanorzea.popularmovies.ui.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.data.MovieMapper.toContentValues
import com.bogdanorzea.popularmovies.data.MoviesContract
import com.bogdanorzea.popularmovies.model.response.MoviesResponse
import com.bogdanorzea.popularmovies.ui.main.PosterAdapter
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils
import com.bogdanorzea.popularmovies.utility.NetworkUtils
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_search.*
import timber.log.Timber

class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var avLoadingIndicatorView: AVLoadingIndicatorView
    private lateinit var warningTextView: TextView

    private var isLoading: Boolean = false
    private var pageNumber: Int = 1
    private lateinit var query: String
    private val posterAdapter = PosterAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = posterAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    loadNextPage()
                }
            }
        })

        avLoadingIndicatorView = findViewById(R.id.avi)
        warningTextView = findViewById(R.id.warning)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            query = intent.getStringExtra(SearchManager.QUERY)
            Timber.d("I started because someone searched for: %s", query)

            loadNextPage()
        }
    }

    private fun loadNextPage() {
        if (NetworkUtils.hasInternetConnection(this)) {
            val url = NetworkUtils.movieSearchUrl(query, pageNumber++)

            val listener = object : AsyncTaskUtils.RequestTaskListener<MoviesResponse> {

                override fun onTaskStarting() {
                    showProgress()
                }

                override fun onTaskComplete(moviesResponse: MoviesResponse?) {
                    if (moviesResponse != null) {
                        posterAdapter.addMovies(moviesResponse.results)

                        for (movie in moviesResponse.results) {
                            contentResolver.insert(MoviesContract.CONTENT_URI, toContentValues(movie))
                        }

                        isLoading = false
                        hideProgress()
                    }
                }
            }

            AsyncTaskUtils.RequestTask(listener, MoviesResponse::class.java).execute(url)
        } else {
            if (posterAdapter.isEmpty) {
                displayWarning(getString(R.string.warning_no_internet))
            } else {
                Toast.makeText(this, getString(R.string.warning_no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProgress() {
        isLoading = true
        avLoadingIndicatorView.smoothToShow()
        hideWarning()
    }

    private fun hideProgress() {
        isLoading = false
        avLoadingIndicatorView.smoothToHide()
    }

    private fun displayWarning(message: String) {
        hideProgress()
        warningTextView.visibility = View.VISIBLE
        warningTextView.text = message
    }

    private fun hideWarning() {
        warningTextView.visibility = View.GONE
    }
}
