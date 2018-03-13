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
import com.bogdanorzea.popularmovies.adapter.CastAdapter;
import com.bogdanorzea.popularmovies.model.response.CreditsResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;


public class MovieCast extends Fragment {
    private AsyncTaskUtils.RequestTaskListener<CreditsResponse> mRequestTaskListener =
            new AsyncTaskUtils.RequestTaskListener<CreditsResponse>() {
                @Override
                public void onTaskStarting() {

                }

                @Override
                public void onTaskComplete(CreditsResponse result) {
                    displayReviews(result);
                }
            };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_reviews_layout, container, false);

        if (getArguments() != null) {
            int movieId = getArguments().getInt("movie_id");

            if (NetworkUtils.hasInternetConnection(getContext())) {
                new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, CreditsResponse.class)
                        .execute(NetworkUtils.movieCreditsUrl(movieId));
            }
        }

        return view;
    }

    private void displayReviews(CreditsResponse result) {
        View view = getView();
        if (view == null) {
            return;
        }

        ListView reviewsListView = view.findViewById(R.id.list);
        ViewCompat.setNestedScrollingEnabled(reviewsListView, true);

        CastAdapter reviewsAdapter = new CastAdapter(getActivity(), result.cast);

        reviewsListView.setAdapter(reviewsAdapter);
    }

}
