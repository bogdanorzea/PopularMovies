package com.bogdanorzea.popularmovies.fragment;

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
import com.bogdanorzea.popularmovies.adapter.CoverAdapter;
import com.bogdanorzea.popularmovies.data.MovieRepository;
import com.bogdanorzea.popularmovies.data.MovieSQLiteRepository;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.model.response.MoviesResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import okhttp3.HttpUrl;

public class MoviesPopular extends Fragment {

    private CoverAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();

        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mAvi = view.findViewById(R.id.avi);
        warningTextView = view.findViewById(R.id.warning);

        mAdapter = new CoverAdapter(context, null);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    loadData();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (NetworkUtils.hasInternetConnection(getContext())) {
            HttpUrl url = NetworkUtils.moviePopularUrl(mAdapter.getNextPageToLoad());

            AsyncTaskUtils.RequestTaskListener<MoviesResponse> listener =
                    new AsyncTaskUtils.RequestTaskListener<MoviesResponse>() {

                        @Override
                        public void onTaskStarting() {
                            showProgress();
                        }

                        @Override
                        public void onTaskComplete(MoviesResponse moviesResponse) {
                            if (moviesResponse != null) {
                                saveMovies(moviesResponse.results);

                                if (0 == mAdapter.getItemCount()) {
                                    mAdapter.setMovies(moviesResponse.results);
                                    mRecyclerView.setAdapter(mAdapter);
                                } else {
                                    mAdapter.addMovies(moviesResponse.results);
                                }

                                hideProgress();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    };

            new AsyncTaskUtils.RequestTask<>(listener, MoviesResponse.class).execute(url);
        } else {
            if (mAdapter.getItemCount() == 0) {
                displayWarning(getString(R.string.warning_no_internet));
            } else {
                Toast.makeText(getContext(), getString(R.string.warning_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveMovies(List<Movie> results) {
        MovieRepository<Movie> repository = new MovieSQLiteRepository(getContext());

        for (Movie movie : results) {
            if (repository.get(movie.id) == null) {
                repository.insert(movie);
            }
        }

    }

    private void showProgress() {
        mAvi.smoothToShow();
        hideWarning();
    }

    private void hideProgress() {
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
