package com.bogdanorzea.popularmovies.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.PosterAdapter;
import com.bogdanorzea.popularmovies.data.MovieMapper;
import com.bogdanorzea.popularmovies.data.MoviesContract;
import com.bogdanorzea.popularmovies.data.RepositoryMovie;
import com.bogdanorzea.popularmovies.data.RepositoryMovieSQLite;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.model.response.MoviesResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

public class PopularTab extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NETWORK_LOADER_ID = 4;
    private PosterAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
    private boolean isLoading = false;
    private List<Integer> loadedIds = new ArrayList<>();
    private int amountIdsAdded = 0;
    private int pageNumber = 1;
    private Parcelable state;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();

        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mAvi = view.findViewById(R.id.avi);
        warningTextView = view.findViewById(R.id.warning);

        mAdapter = new PosterAdapter(context);

        if (savedInstanceState != null) {
            loadedIds = savedInstanceState.getIntegerArrayList("loaded_ids");
            pageNumber = savedInstanceState.getInt("page_number");
            state = savedInstanceState.getParcelable("recycler_view_state");
        }

        mRecyclerView = view.findViewById(R.id.recycler_view);
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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (loadedIds.isEmpty()) {
            loadNextPage();
        } else {
            getLoaderManager().restartLoader(NETWORK_LOADER_ID, null, this);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("loaded_ids", (ArrayList<Integer>) loadedIds);
        outState.putInt("page_number", pageNumber);
        outState.putParcelable("recycler_view_state", mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void loadNextPage() {
        if (NetworkUtils.hasInternetConnection(getContext())) {
            HttpUrl url = NetworkUtils.moviePopularUrl(pageNumber++);

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

                                for (Movie movie : moviesResponse.results) {
                                    loadedIds.add(movie.id);
                                }
                                amountIdsAdded = moviesResponse.results.size();

                                getLoaderManager().restartLoader(NETWORK_LOADER_ID, null, PopularTab.this);
                                isLoading = false;
                                hideProgress();
                            }
                        }
                    };

            new AsyncTaskUtils.RequestTask<>(listener, MoviesResponse.class).execute(url);
        } else {
            if (loadedIds.isEmpty()) {
                displayWarning(getString(R.string.warning_no_internet));
            } else {
                Toast.makeText(getContext(), getString(R.string.warning_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveMovies(List<Movie> results) {
        RepositoryMovie<Movie> repository = new RepositoryMovieSQLite(getContext());

        for (Movie movie : results) {
            if (repository.get(movie.id) == null) {
                repository.insert(movie);
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        List<Integer> newIds;
        if (amountIdsAdded > 0) {
            newIds = loadedIds.subList(loadedIds.size() - amountIdsAdded, loadedIds.size());
        } else {
            newIds = loadedIds;
        }

        return new CursorLoader(getActivity(),
                MoviesContract.CONTENT_URI,
                null,
                MoviesContract.MovieEntry._ID + " IN (" + toPlaceHolderString(newIds) + ")",
                toStringArray(newIds),
                MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Movie> moviesIds = new ArrayList<>();
        if (data != null) {
            for (int i = 0, size = data.getCount(); i < size; i++) {
                data.moveToPosition(i);

                moviesIds.add(MovieMapper.fromCursor(data));
            }
        }

        mAdapter.addMovies(moviesIds);
        if (state != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
            state = null;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    String toPlaceHolderString(List<Integer> movieIds) {
        int size = movieIds.size();

        if (size > 1) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < size; i++) {
                result.append("?").append(",");
            }

            return result.substring(0, result.length() - 1);
        } else {
            return "";
        }
    }

    String[] toStringArray(List<Integer> movieIds) {
        String[] ret = new String[movieIds.size()];

        for (int i = 0; i < movieIds.size(); i++) {
            ret[i] = movieIds.get(i).toString();
        }

        return ret;
    }
}
