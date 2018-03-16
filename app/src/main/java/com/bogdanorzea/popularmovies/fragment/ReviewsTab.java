package com.bogdanorzea.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.ReviewsAdapter;
import com.bogdanorzea.popularmovies.model.response.ReviewsResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;


public class ReviewsTab extends Fragment {
    private int mMovieId = -1;
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
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
                new AsyncTaskUtils.RequestTask<>(mMovieReviewsAsyncTaskListener, ReviewsResponse.class)
                        .execute(NetworkUtils.movieReviewsUrl(mMovieId, 1));
            } else {
                displayWarning(getString(R.string.warning_no_internet));
            }
        }
    }

    private void displayReviews(ReviewsResponse result) {
        View view = getView();
        if (view == null) {
            return;
        }

        if (result.results.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data));
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

    private void displayWarning(String message) {
        warningTextView.setVisibility(View.VISIBLE);
        warningTextView.setText(message);
    }

    private void hideWarning() {
        warningTextView.setVisibility(View.GONE);
    }
}
