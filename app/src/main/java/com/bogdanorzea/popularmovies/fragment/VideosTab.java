package com.bogdanorzea.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.VideosAdapter;
import com.bogdanorzea.popularmovies.model.object.Video;
import com.bogdanorzea.popularmovies.model.response.VideosResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

public class VideosTab extends Fragment {
    private int mMovieId = -1;
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
    private AsyncTaskUtils.RequestTaskListener<VideosResponse> mRequestTaskListener =
            new AsyncTaskUtils.RequestTaskListener<VideosResponse>() {
                @Override
                public void onTaskStarting() {
                    showProgress();
                }

                @Override
                public void onTaskComplete(VideosResponse result) {
                    hideProgress();
                    displayVideos(result);
                }
            };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list_view, container, false);
        mAvi = view.findViewById(R.id.avi);
        warningTextView = view.findViewById(R.id.warning);

        if (getArguments() != null) {
            mMovieId = getArguments().getInt("movie_id");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMovieId != -1) {
            if (NetworkUtils.hasInternetConnection(getContext())) {
                new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, VideosResponse.class)
                        .execute(NetworkUtils.movieVideosUrl(mMovieId));
            } else {
                displayWarning(getString(R.string.warning_no_internet));
            }
        }
    }

    private void displayVideos(VideosResponse result) {
        View view = getView();
        if (view == null) {
            return;
        }

        if (result.results.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data));
        }

        ListView reviewsListView = view.findViewById(R.id.list);
        ViewCompat.setNestedScrollingEnabled(reviewsListView, true);

        VideosAdapter videosAdapter = new VideosAdapter(getActivity(), result.results);

        reviewsListView.setAdapter(videosAdapter);

        reviewsListView.setOnItemClickListener((adapterView, view1, position, id) -> {
            Video video = (Video) adapterView.getItemAtPosition(position);

            if (video.isVideoOnYoutube()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getYoutubeVideoUrl()));

                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), R.string.failed_launch_video, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showProgress() {
        mAvi.smoothToShow();
    }

    private void hideProgress() {
        mAvi.smoothToHide();
    }

    private void displayWarning(String message) {
        warningTextView.setVisibility(View.VISIBLE);
        warningTextView.setText(message);
    }
}
