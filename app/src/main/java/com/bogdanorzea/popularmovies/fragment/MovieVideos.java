package com.bogdanorzea.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.VideosAdapter;
import com.bogdanorzea.popularmovies.model.response.VideosResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

public class MovieVideos extends Fragment {

    private AsyncTaskUtils.RequestTaskListener<VideosResponse> mRequestTaskListener =
            new AsyncTaskUtils.RequestTaskListener<VideosResponse>() {
                @Override
                public void onTaskStarting() {
                }

                @Override
                public void onTaskComplete(VideosResponse result) {
                    displayVideos(result);
                }
            };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_reviews_layout, container, false);

        if (getArguments() != null) {
            int movieId = getArguments().getInt("movie_id");

            new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, VideosResponse.class)
                    .execute(NetworkUtils.movieVideosUrl(movieId));
        }

        return view;
    }

    private void displayVideos(VideosResponse result) {
        View view = getView();
        if (view == null) {
            return;
        }

        ListView reviewsListView = view.findViewById(R.id.list);
        ViewCompat.setNestedScrollingEnabled(reviewsListView, true);

        VideosAdapter videosAdapter = new VideosAdapter(getActivity(), result.results);

        reviewsListView.setAdapter(videosAdapter);


//        for (int i = 0; i < mCurrentVideosResponse.results.size(); i++) {
//            Video result = mCurrentVideosResponse.results.get(i);
//            if (result.site.equalsIgnoreCase("YouTube") &&
//                    result.type.equalsIgnoreCase("trailer")) {
//                String youtubeLink = "https://www.youtube.com/watch?v=" + result.key;
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
//                startActivity(intent);
//                return;
//            }
//        }
    }
}
