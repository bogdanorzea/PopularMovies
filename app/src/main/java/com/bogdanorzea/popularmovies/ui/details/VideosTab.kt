package com.bogdanorzea.popularmovies.ui.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.model.`object`.Video
import com.bogdanorzea.popularmovies.model.response.VideosResponse
import com.bogdanorzea.popularmovies.utility.NetworkUtils
import com.bogdanorzea.popularmovies.utility.hasInternetConnection
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

class VideosTab : Fragment() {
    companion object {
        private const val SAVED_LIST = "saved_list"
    }

    private lateinit var adapter: VideosAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = VideosAdapter(context!!)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        if (savedInstanceState != null) {
            val videos =
                    savedInstanceState.getParcelableArrayList<Video>(SAVED_LIST).toList()

            displayVideos(videos)
        } else {
            if (arguments != null) {
                val movieId = arguments?.getInt("movie_id") ?: -1

                if (context?.hasInternetConnection() == true) {
                    launch(UI) {
                        showProgress()

                        val response = async(CommonPool) {
                            NetworkUtils.fetchResponse(
                                    NetworkUtils.movieVideosUrl(movieId),
                                    VideosResponse::class.java)
                        }.await()

                        hideProgress()
                        response?.results?.let {
                            displayVideos(it)
                        }
                    }
                } else {
                    displayWarning(getString(R.string.warning_no_internet))
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_LIST, adapter.videos as ArrayList<Video>)
    }

    private fun displayVideos(list: List<Video>) {
        if (list.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data))
        } else {
            adapter.addVideos(list)
        }
    }

    private fun showProgress() {
        avLoadingIndicator?.smoothToShow()
    }

    private fun hideProgress() {
        avLoadingIndicator?.smoothToHide()
    }

    private fun displayWarning(message: String) {
        warningTextView.visibility = View.VISIBLE
        warningTextView.text = message
    }

}
