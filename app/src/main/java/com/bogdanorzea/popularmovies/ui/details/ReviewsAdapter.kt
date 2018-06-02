package com.bogdanorzea.popularmovies.ui.details

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.model.`object`.Review
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_review.*

class ReviewsAdapter(private val context: Context) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
    val reviews: MutableList<Review> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindReview(reviews[position])
    }

    override fun getItemCount() = reviews.size

    fun addReviews(data: List<Review>) {
        val size = itemCount
        reviews.addAll(data)
        if (size > 0) {
            notifyItemRangeInserted(size, data.size)
        } else {
            notifyDataSetChanged()
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), View.OnClickListener, LayoutContainer {

        fun bindReview(review: Review) {
            author.text = review.author
            content.text = review.content

            containerView.tag = review
            containerView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val context = view.context

            val (_, _, _, url) = itemView.tag as Review

            if (!url.isNullOrEmpty()) {
                CustomTabsIntent.Builder()
                        .build()
                        .launchUrl(context, Uri.parse(url))
            } else {
                Toast.makeText(context, "Couldn't find the review url", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
