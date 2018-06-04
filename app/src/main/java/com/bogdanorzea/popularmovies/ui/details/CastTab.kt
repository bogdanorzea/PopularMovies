package com.bogdanorzea.popularmovies.ui.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.model.`object`.Cast
import com.bogdanorzea.popularmovies.model.response.CreditsResponse
import com.bogdanorzea.popularmovies.utility.NetworkUtils
import com.bogdanorzea.popularmovies.utility.hasInternetConnection
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

class CastTab : Fragment() {
    companion object {
        private const val SAVED_LIST = "saved_list"
    }

    private lateinit var adapter: CastAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CastAdapter(context!!)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        if (savedInstanceState != null) {
            val castList =
                    savedInstanceState.getParcelableArrayList<Cast>(SAVED_LIST).toList()

            displayCredits(castList)
        } else {
            if (arguments != null) {
                val movieId = arguments?.getInt("movie_id") ?: -1

                if (context?.hasInternetConnection() == true) {
                    launch(UI) {
                        showProgress()

                        val creditsResponse: CreditsResponse? = async(CommonPool) {
                            NetworkUtils.fetchResponse(
                                    NetworkUtils.movieCreditsUrl(movieId),
                                    CreditsResponse::class.java)
                        }.await()

                        hideProgress()
                        creditsResponse?.cast?.let {
                            displayCredits(it)
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
        outState.putParcelableArrayList(SAVED_LIST, adapter.castList as ArrayList<Cast>)
    }

    private fun displayCredits(list: List<Cast>) {
        if (list.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data))
        } else {
            adapter.addCast(list)
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
