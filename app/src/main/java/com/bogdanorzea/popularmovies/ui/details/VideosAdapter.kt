package com.bogdanorzea.popularmovies.ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.model.`object`.Video
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_video.*

class VideosAdapter(private val context: Context) : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    val videos: MutableList<Video> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindVideo(videos[position])
    }

    override fun getItemCount() = videos.size

    fun addVideos(data: List<Video>) {
        val size = itemCount
        videos.addAll(data)
        if (size > 0) {
            notifyItemRangeInserted(size, data.size)
        } else {
            notifyDataSetChanged()
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), View.OnClickListener, LayoutContainer {

        fun bindVideo(video: Video) {
            videoName.text = video.name

            Picasso.with(containerView.context)
                    .load(video.getYoutubeThumbnailLink())
                    .into(videoThumbnail)

            containerView.tag = video
            containerView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val context = view.context

            val video = itemView.tag as Video

            if (video.isVideoOnYoutube()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.getYoutubeVideoUrl()))

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, R.string.failed_launch_video, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
