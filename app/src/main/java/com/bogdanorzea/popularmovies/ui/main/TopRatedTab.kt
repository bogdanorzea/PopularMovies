package com.bogdanorzea.popularmovies.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.data.MoviesContract
import com.bogdanorzea.popularmovies.data.toContentValues
import com.bogdanorzea.popularmovies.model.`object`.Movie
import com.bogdanorzea.popularmovies.model.response.MoviesResponse
import com.bogdanorzea.popularmovies.utility.NetworkUtils
import com.bogdanorzea.popularmovies.utility.hasInternetConnection
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

class TopRatedTab : Fragment() {

    private lateinit var adapter: PosterAdapter
    private var isLoading = false
    private var pageNumber = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = PosterAdapter(context!!)

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView!!.canScrollVertically(1) && !isLoading) {
                    loadNextPage()
                }
            }
        })

        if (savedInstanceState != null) {
            val movieList =
                    savedInstanceState.getParcelableArrayList<Movie>("movie_array").toList()
            pageNumber = savedInstanceState.getInt("page_number")
            adapter.addMovies(movieList)
        } else {
            loadNextPage()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("movie_array", adapter.movies as ArrayList<Movie>)
        outState.putInt("page_number", pageNumber)
    }

    private fun loadNextPage() {
        if (context?.hasInternetConnection() == true) {
            launch(UI) {
                showProgress()
                val response = async(CommonPool) {
                    NetworkUtils.fetchResponse(
                            NetworkUtils.movieTopRatedUrl(pageNumber++),
                            MoviesResponse::class.java)
                }.await()

                response?.results?.let {
                    adapter.addMovies(it)

                    it.forEach {
                        this@TopRatedTab.context?.contentResolver?.insert(MoviesContract.CONTENT_URI, it.toContentValues())
                    }
                }

                isLoading = false
                hideProgress()
            }

        } else {
            if (adapter.isEmpty()) {
                displayWarning(getString(R.string.warning_no_internet))
            } else {
                Toast.makeText(context, getString(R.string.warning_no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProgress() {
        isLoading = true
        avLoadingIndicator?.smoothToShow()
        hideWarning()
    }

    private fun hideProgress() {
        isLoading = false
        avLoadingIndicator?.smoothToHide()
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
