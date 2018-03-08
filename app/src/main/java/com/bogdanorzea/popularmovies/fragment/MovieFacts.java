package com.bogdanorzea.popularmovies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.DataUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

import static com.bogdanorzea.popularmovies.utility.DataUtils.formatDuration;
import static com.bogdanorzea.popularmovies.utility.DataUtils.formatMoney;


public class MovieFacts extends Fragment {
    private int movieId;
    private Movie mCurrentMovie;
    private View view;
    private AsyncTaskUtils.AsyncTaskListener<Movie> mMovieAsyncTaskListener =
            new AsyncTaskUtils.AsyncTaskListener<Movie>() {
                @Override
                public void onTaskStarting() {
                }

                @Override
                public void onTaskComplete(Movie movie) {
                    mCurrentMovie = movie;
                    displayFacts();
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.movie_facts_layout, container, false);

        Bundle arguments = getArguments();
        movieId = arguments.getInt("movie_id");

        new AsyncTaskUtils.MovieDetailsAsyncTask(mMovieAsyncTaskListener)
                .execute(NetworkUtils.movieDetailsUrl(movieId));

        return view;
    }

    private void displayFacts() {
        ScrollView scrollView = view.findViewById(R.id.scroll_view);
        ViewCompat.setNestedScrollingEnabled(scrollView, true);

        ((TextView) view.findViewById(R.id.movie_runtime)).setText(formatDuration(mCurrentMovie.runtime));
        ((TextView) view.findViewById(R.id.movie_budget)).setText(formatMoney(mCurrentMovie.budget));
        ((TextView) view.findViewById(R.id.movie_revenue)).setText(formatMoney(mCurrentMovie.revenue));
        ((TextView) view.findViewById(R.id.movie_genre)).setText(DataUtils.printGenres(mCurrentMovie.genres));
    }
}
