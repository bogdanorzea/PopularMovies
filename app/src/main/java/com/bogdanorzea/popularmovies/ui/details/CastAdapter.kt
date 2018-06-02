package com.bogdanorzea.popularmovies.ui.details

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.model.`object`.Cast
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cast.*

class CastAdapter(private val context: Context) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {
    val castList: MutableList<Cast> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_cast, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCast(castList[position])
    }

    override fun getItemCount() = castList.size

    fun addCast(data: List<Cast>) {
        val size = itemCount
        castList.addAll(data)
        if (size > 0) {
            notifyItemRangeInserted(size, data.size)
        } else {
            notifyDataSetChanged()
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindCast(cast: Cast) {
            name.text = cast.name
            character.text = cast.character

            Picasso.with(containerView.context)
                    .load(cast.getProfileUrl())
                    .error(R.drawable.missing_cover)
                    .into(profileImage)
        }

    }

}

