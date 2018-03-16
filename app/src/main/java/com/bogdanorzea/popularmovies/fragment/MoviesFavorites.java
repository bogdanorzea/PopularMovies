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

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.CoverAdapter;
import com.bogdanorzea.popularmovies.data.MovieRepository;
import com.bogdanorzea.popularmovies.data.MovieSQLiteRepository;
import com.bogdanorzea.popularmovies.data.MoviesContract;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.DataUtils;

import java.util.List;

public class MoviesFavorites extends Fragment {

    private RecyclerView mRecyclerView;
    private CoverAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();

        View view = inflater.inflate(R.layout.layout_reviclerview, container, false);

        mAdapter = new CoverAdapter(context, null);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setMovies(loadSQLData(getContext()));
        mAdapter.notifyDataSetChanged();
    }

    private List<Movie> loadSQLData(Context context) {
        MovieRepository<Movie> repository = new MovieSQLiteRepository(context);

        String preferredSortRule = DataUtils.getPreferredSortRule(context);
        String sortOrder = null;
        if (preferredSortRule.equals(getString(R.string.pref_sort_by_popularity))) {
            sortOrder = MoviesContract.MovieEntry.COLUMN_NAME_POPULARITY + " DESC";
        } else if (preferredSortRule.equals(getString(R.string.pref_sort_by_top_rated))) {
            sortOrder = MoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " DESC";

        }

        return repository.getFavorites(sortOrder);
    }
}
