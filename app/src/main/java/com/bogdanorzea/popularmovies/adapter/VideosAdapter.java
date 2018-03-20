package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private Context context;
    private List<Video> mVideos;

    public VideosAdapter(@NonNull Context context) {
        this.context = context;
        this.mVideos = new ArrayList<>();
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void addVideos(@NonNull List<Video> data) {
        int size = getItemCount();
        mVideos.addAll(data);
        if (size > 0) {
            notifyItemRangeInserted(size, data.size());
        } else {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);

        return new VideosAdapter.ViewHolder(layout);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.ViewHolder holder, int position) {
        Video video = mVideos.get(position);

        holder.title.setText(video.name);
        holder.itemView.setTag(video);

        Picasso.with(context)
                .load(video.getYoutubeThumbnailLink())
                .into(holder.thumbnail);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title;
        final ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.text);
            thumbnail = view.findViewById(R.id.image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Video video = (Video) itemView.getTag();

            if (video.isVideoOnYoutube()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getYoutubeVideoUrl()));

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, R.string.failed_launch_video, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
