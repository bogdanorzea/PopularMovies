package com.bogdanorzea.popularmovies.ui.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.model.`object`.Review
import com.bogdanorzea.popularmovies.model.response.ReviewsResponse
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils
import com.bogdanorzea.popularmovies.utility.NetworkUtils
import com.bogdanorzea.popularmovies.utility.hasInternetConnection
import kotlinx.android.synthetic.main.layout_recycler_view.*
import java.util.*

class ReviewsTab : Fragment() {
    companion object {
        private const val SAVED_LIST = "saved_list"
    }

    private lateinit var adapter: ReviewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ReviewsAdapter(context!!)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        if (savedInstanceState != null) {
            val reviews =
                    savedInstanceState.getParcelableArrayList<Review>(SAVED_LIST).toList()

            displayReviews(reviews)
        } else {
            if (arguments != null) {
                val movieId = arguments?.getInt("movie_id") ?: -1

                if (context?.hasInternetConnection() == true) {
                    val mRequestTaskListener = object : AsyncTaskUtils.RequestTaskListener<ReviewsResponse> {
                        override fun onTaskStarting() {
                            showProgress()
                        }

                        override fun onTaskComplete(result: ReviewsResponse?) {
                            hideProgress()
                            result?.results?.let {
                                displayReviews(it)
                            }
                        }
                    }

                    AsyncTaskUtils.RequestTask(mRequestTaskListener, ReviewsResponse::class.java)
                            .execute(NetworkUtils.movieReviewsUrl(movieId, 1))
                } else {
                    displayWarning(getString(R.string.warning_no_internet))
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_LIST, adapter.reviews as ArrayList<Review>)
    }

    private fun displayReviews(list: List<Review>) {
        if (list.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data))
        } else {
            adapter.addReviews(list)
        }
    }

    private fun showProgress() {
        avLoadingIndicator.smoothToShow()
    }

    private fun hideProgress() {
        avLoadingIndicator.smoothToHide()
    }

    private fun displayWarning(message: String) {
        warningTextView.visibility = View.VISIBLE
        warningTextView.text = message
    }

}
