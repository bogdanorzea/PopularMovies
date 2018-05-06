package com.bogdanorzea.popularmovies.ui.main;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.data.MovieMapper;
import com.bogdanorzea.popularmovies.data.MoviesContract;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoritesTab extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String DESC = " DESC";
    private static final int LOADER_ID = 0;
    private RecyclerView mRecyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String preferredSortRule = DataUtils.getPreferredSortRule(getActivity());
        String sortOrder = null;
        if (preferredSortRule.equals(getString(R.string.pref_sort_by_popularity))) {
            sortOrder = MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY + DESC;
        } else if (preferredSortRule.equals(getString(R.string.pref_sort_by_top_rated))) {
            sortOrder = MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + DESC;
        }

        return new CursorLoader(getActivity(), MoviesContract.CONTENT_URI, null, MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE + " = ?", new String[]{"1"}, sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        PosterAdapter mAdapter = new PosterAdapter(getContext());

        List<Movie> moviesIds = new ArrayList<>();
        if (data != null) {
            for (int i = 0, size = data.getCount(); i < size; i++) {
                data.moveToPosition(i);

                moviesIds.add(MovieMapper.fromCursor(data));
            }
        }

        mAdapter.addMovies(moviesIds);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
