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
import com.bogdanorzea.popularmovies.adapter.ReviewsAdapter;
import com.bogdanorzea.popularmovies.model.response.ReviewsResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;


public class ReviewsTab extends Fragment {
    private AVLoadingIndicatorView mAvi;

    private AsyncTaskUtils.RequestTaskListener<ReviewsResponse> mMovieReviewsAsyncTaskListener =
            new AsyncTaskUtils.RequestTaskListener<ReviewsResponse>() {
                @Override
                public void onTaskStarting() {
                    showProgress();
                }

                @Override
                public void onTaskComplete(ReviewsResponse result) {
                    hideProgress();
                    displayReviews(result);
                }
            };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list_view, container, false);
        mAvi = view.findViewById(R.id.avi);

        if (getArguments() != null) {
            int movieId = getArguments().getInt("movie_id");

            if (NetworkUtils.hasInternetConnection(getContext())) {
                new AsyncTaskUtils.RequestTask<>(mMovieReviewsAsyncTaskListener, ReviewsResponse.class)
                        .execute(NetworkUtils.movieReviewsUrl(movieId, 1));
            }
        }

        return view;
    }

    private void displayReviews(ReviewsResponse result) {
        View view = getView();
        if (view == null) {
            return;
        }

        ListView reviewsListView = view.findViewById(R.id.list);
        ViewCompat.setNestedScrollingEnabled(reviewsListView, true);

        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), result.results);

        reviewsListView.setAdapter(reviewsAdapter);
    }

    private void showProgress() {
        mAvi.smoothToShow();
    }

    private void hideProgress() {
        mAvi.smoothToHide();
    }
}
