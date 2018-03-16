package com.bogdanorzea.popularmovies.fragment;

import android.os.Bundle;
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


public class MovieReviews extends Fragment {
    private AsyncTaskUtils.AsyncTaskListener<ReviewsResponse> mMovieReviewsAsyncTaskListener =
            new AsyncTaskUtils.AsyncTaskListener<ReviewsResponse>() {
                @Override
                public void onTaskStarting() {

                }

                @Override
                public void onTaskComplete(ReviewsResponse result) {
                    displayReviews(result);
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_reviews_layout, container, false);

        if (getArguments() != null) {
            int movieId = getArguments().getInt("movie_id");

            new AsyncTaskUtils.MovieReviewsAsyncTask(mMovieReviewsAsyncTaskListener)
                    .execute(NetworkUtils.movieReviewsUrl(movieId, 1));
        }

        return view;
    }

    private void displayReviews(ReviewsResponse result) {
        View view = getView();

        ListView reviewsListView = view.findViewById(R.id.list);
        ViewCompat.setNestedScrollingEnabled(reviewsListView, true);

        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), result.results);

        reviewsListView.setAdapter(reviewsAdapter);
    }

}
