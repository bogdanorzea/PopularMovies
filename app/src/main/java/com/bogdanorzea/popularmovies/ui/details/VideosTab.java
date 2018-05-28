package com.bogdanorzea.popularmovies.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Video;
import com.bogdanorzea.popularmovies.model.response.VideosResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class VideosTab extends Fragment {
    private static final String SAVED_LIST = "saved_list";
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
    private VideosAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mAvi = view.findViewById(R.id.avi);
        warningTextView = view.findViewById(R.id.warning);

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideosAdapter(getActivity());

        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            ArrayList<Video> videos = savedInstanceState.getParcelableArrayList(SAVED_LIST);

            displayVideos(videos);
        } else {
            if (getArguments() != null) {
                int movieId = getArguments().getInt("movie_id");

                if (NetworkUtils.hasInternetConnection(getContext())) {
                    AsyncTaskUtils.RequestTaskListener<VideosResponse> mRequestTaskListener =
                            new AsyncTaskUtils.RequestTaskListener<VideosResponse>() {
                                @Override
                                public void onTaskStarting() {
                                    showProgress();
                                }

                                @Override
                                public void onTaskComplete(VideosResponse result) {
                                    hideProgress();
                                    displayVideos(result.getResults());
                                }
                            };

                    new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, VideosResponse.class)
                            .execute(NetworkUtils.movieVideosUrl(movieId));
                } else {
                    displayWarning(getString(R.string.warning_no_internet));
                }
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_LIST, (ArrayList<Video>) mAdapter.getVideos());
    }

    private void displayVideos(List<Video> list) {
        if (list.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data));
        } else {
            mAdapter.addVideos(list);
        }
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
