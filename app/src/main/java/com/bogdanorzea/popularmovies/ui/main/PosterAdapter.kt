package com.bogdanorzea.popularmovies.ui.main

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.model.`object`.Movie
import com.bogdanorzea.popularmovies.ui.details.DetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_poster.*

class PosterAdapter(private val context: Context) : RecyclerView.Adapter<PosterAdapter.ViewHolder>() {
    val movies: MutableList<Movie> = mutableListOf()

    fun isEmpty(): Boolean = movies.size < 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_poster, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMovie(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun addMovies(data: List<Movie>) {
        val size = itemCount
        movies.addAll(data)
        if (size > 0) {
            notifyItemRangeInserted(size, data.size)
        } else {
            notifyDataSetChanged()
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer, View.OnClickListener {

        fun bindMovie(movie: Movie) {
            Picasso.with(containerView.context)
                    .load(movie.getPosterUrl())
                    .error(R.drawable.missing_cover)
                    .into(poster)

            containerView.tag = movie
            containerView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val context = view.context

            val (_, _, _, _, id) = itemView.tag as Movie

            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.MOVIE_ID, id)
            context.startActivity(intent)
        }
    }

}
