package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.model.object.Video;

import java.util.List;

public class VideosAdapter extends ArrayAdapter<Video> {

    private List<Video> mVideos;

    public VideosAdapter(@NonNull Context context, List<Video> videos) {
        super(context, 0, videos);
        this.mVideos = videos;
    }

    @Override
    public int getCount() {
        if (mVideos == null) {
            return 0;
        }

        return mVideos.size();
    }

    @Override
    public Video getItem(int i) {
        if (mVideos == null) {
            return null;
        }

        return mVideos.get(i);
    }

    @NonNull
    @Override
    public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, viewGroup, false);
        }

        Video video = mVideos.get(i);
        TextView title = view.findViewById(android.R.id.text1);
        TextView link = view.findViewById(android.R.id.text2);

        title.setText(video.name);

        String youtubeLink = "";
        if (video.site.equalsIgnoreCase("YouTube")) {
            youtubeLink = "https://www.youtube.com/watch?v=" + video.key;
        }

        link.setText(youtubeLink);

        return view;
    }
}
