package com.bogdanorzea.popularmovies.fragment;

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
import com.bogdanorzea.popularmovies.adapter.ReviewsAdapter;
import com.bogdanorzea.popularmovies.model.object.Review;
import com.bogdanorzea.popularmovies.model.response.ReviewsResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsTab extends Fragment {
    private static final String SAVED_LIST = "saved_list";
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
    private ReviewsAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mAvi = view.findViewById(R.id.avi);
        warningTextView = view.findViewById(R.id.warning);

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ReviewsAdapter(getActivity());

        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            ArrayList<Review> reviews = savedInstanceState.getParcelableArrayList(SAVED_LIST);

            displayReviews(reviews);
        } else {
            if (getArguments() != null) {
                int movieId = getArguments().getInt("movie_id");

                if (NetworkUtils.hasInternetConnection(getContext())) {
                    AsyncTaskUtils.RequestTaskListener<ReviewsResponse> mRequestTaskListener =
                            new AsyncTaskUtils.RequestTaskListener<ReviewsResponse>() {
                                @Override
                                public void onTaskStarting() {
                                    showProgress();
                                }

                                @Override
                                public void onTaskComplete(ReviewsResponse result) {
                                    hideProgress();
                                    displayReviews(result.results);
                                }
                            };

                    new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, ReviewsResponse.class)
                            .execute(NetworkUtils.movieReviewsUrl(movieId, 1));
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
        outState.putParcelableArrayList(SAVED_LIST, (ArrayList<Review>) mAdapter.getReviews());
    }

    private void displayReviews(List<Review> list) {
        if (list.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data));
        } else {
            mAdapter.addReviews(list);
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
