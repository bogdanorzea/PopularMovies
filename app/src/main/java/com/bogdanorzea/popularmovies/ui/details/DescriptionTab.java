package com.bogdanorzea.popularmovies.ui.details;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.data.MoviesContract;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.DataUtils;
import com.squareup.picasso.Picasso;

import static com.bogdanorzea.popularmovies.data.MovieMapperKt.toMovie;
import static com.bogdanorzea.popularmovies.utility.DataUtils.formatDuration;
import static com.bogdanorzea.popularmovies.utility.DataUtils.formatMoney;


public class DescriptionTab extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 2;
    private int mMovieId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_description, container, false);
        ScrollView scrollView = view.findViewById(R.id.scroll_view);
        ViewCompat.setNestedScrollingEnabled(scrollView, true);

        if (getArguments() != null) {
            mMovieId = getArguments().getInt("movie_id");
        }

        return view;
    }

    private void displayDescription(Cursor cursor) {
        View view = getView();
        if (view == null) {
            return;
        }

        if (cursor.moveToFirst()) {
            Movie movie = toMovie(cursor);

            // Poster
            ImageView poster = view.findViewById(R.id.poster);
            Picasso.with(getContext())
                    .load(movie.getPosterUrl())
                    .error(R.drawable.missing_cover)
                    .into(poster);

            // Release date
            ((TextView) view.findViewById(R.id.release_date))
                    .setText(DataUtils.addParenthesis(movie.getYear()));

            // Title
            ((TextView) view.findViewById(R.id.title)).setText(movie.getTitle());

            // Tagline
            ((TextView) view.findViewById(R.id.tagline)).setText(
                    DataUtils.quoteString(movie.getTagline()));

            // Overview
            ((TextView) view.findViewById(R.id.overview)).setText(movie.getOverview());

            // Score
            ((RatingBar) view.findViewById(R.id.score))
                    .setRating((float) movie.getVoteAverage() / 2);

            // Runtime
            ((TextView) view.findViewById(R.id.runtime)).setText(formatDuration(movie.getRuntime()));

            // Budget
            ((TextView) view.findViewById(R.id.budget)).setText(formatMoney(movie.getBudget()));

            // Revenue
            ((TextView) view.findViewById(R.id.revenue)).setText(formatMoney(movie.getRevenue()));

            // Genre
            ((TextView) view.findViewById(R.id.genre)).setText(movie.printGenres());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(mMovieId));

        return new CursorLoader(getActivity(), movieUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        displayDescription(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader = null;
    }
}
