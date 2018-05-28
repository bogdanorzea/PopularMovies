package com.bogdanorzea.popularmovies.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.data.MoviesContract;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.model.response.MoviesResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import okhttp3.HttpUrl;

import static com.bogdanorzea.popularmovies.data.MovieMapper.toContentValues;

public class TopRatedTab extends Fragment {

    private PosterAdapter mAdapter;
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
    private boolean isLoading = false;
    private int pageNumber = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();

        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mAvi = view.findViewById(R.id.avi);
        warningTextView = view.findViewById(R.id.warning);

        mAdapter = new PosterAdapter(context);

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    loadNextPage();
                }
            }
        });

        if (savedInstanceState != null) {
            ArrayList<Movie> movieList = savedInstanceState.getParcelableArrayList("movie_array");
            pageNumber = savedInstanceState.getInt("page_number");
            mAdapter.addMovies(movieList);
        } else {
            loadNextPage();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie_array", (ArrayList<Movie>) mAdapter.getMovies());
        outState.putInt("page_number", pageNumber);
    }

    private void loadNextPage() {
        if (NetworkUtils.hasInternetConnection(getContext())) {
            HttpUrl url = NetworkUtils.movieTopRatedUrl(pageNumber++);

            AsyncTaskUtils.RequestTaskListener<MoviesResponse> listener =
                    new AsyncTaskUtils.RequestTaskListener<MoviesResponse>() {

                        @Override
                        public void onTaskStarting() {
                            showProgress();
                        }

                        @Override
                        public void onTaskComplete(MoviesResponse moviesResponse) {
                            if (moviesResponse != null) {

                                mAdapter.addMovies(moviesResponse.getResults());

                                for (Movie movie : moviesResponse.getResults()) {
                                    getContext().getContentResolver().insert(MoviesContract.CONTENT_URI, toContentValues(movie));
                                }

                                isLoading = false;
                                hideProgress();
                            }
                        }
                    };

            new AsyncTaskUtils.RequestTask<>(listener, MoviesResponse.class).execute(url);
        } else {
            if (mAdapter.isEmpty()) {
                displayWarning(getString(R.string.warning_no_internet));
            } else {
                Toast.makeText(getContext(), getString(R.string.warning_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showProgress() {
        isLoading = true;
        mAvi.smoothToShow();
        hideWarning();
    }

    private void hideProgress() {
        isLoading = false;
        mAvi.smoothToHide();
    }

    private void displayWarning(String message) {
        hideProgress();
        warningTextView.setVisibility(View.VISIBLE);
        warningTextView.setText(message);
    }

    private void hideWarning() {
        warningTextView.setVisibility(View.GONE);
    }

}
