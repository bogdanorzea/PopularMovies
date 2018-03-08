package com.bogdanorzea.popularmovies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.DataUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;


public class MovieDescription extends Fragment {

    private AsyncTaskUtils.AsyncTaskListener<Movie> mMovieAsyncTaskListener =
            new AsyncTaskUtils.AsyncTaskListener<Movie>() {
                @Override
                public void onTaskStarting() {
                }

                @Override
                public void onTaskComplete(Movie movie) {
                    displayDescription(movie);
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_description_layout, container, false);

        Bundle arguments = getArguments();
        int movieId = arguments.getInt("movie_id");

        new AsyncTaskUtils.MovieDetailsAsyncTask(mMovieAsyncTaskListener)
                .execute(NetworkUtils.movieDetailsUrl(movieId));

        return view;
    }

    private void displayDescription(Movie movie) {
        View view = getView();

        ScrollView scrollView = view.findViewById(R.id.scroll_view);
        ViewCompat.setNestedScrollingEnabled(scrollView, true);

        // Poster
        ImageView poster = view.findViewById(R.id.movie_cover);
        NetworkUtils.loadPoster(getActivity(), poster, movie.posterPath);

        // Release date
        ((TextView) view.findViewById(R.id.movie_release_date)).setText(
                String.format("(%s)", movie.releaseDate.substring(0, 4)));
        // Title
        ((TextView) view.findViewById(R.id.movie_title)).setText(movie.title);

        // Tagline
        ((TextView) view.findViewById(R.id.movie_tagline)).setText(
                DataUtils.quoteString(movie.tagline));
        // Overview
        ((TextView) view.findViewById(R.id.movie_overview)).setText(movie.overview);

        // Score
        ((RatingBar) view.findViewById(R.id.movie_score)).setRating(movie.voteAverage / 2);
    }


}
