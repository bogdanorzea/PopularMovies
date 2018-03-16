package com.bogdanorzea.popularmovies.fragment;

import android.graphics.BitmapFactory;
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
import com.bogdanorzea.popularmovies.data.MovieRepository;
import com.bogdanorzea.popularmovies.data.MovieSQLiteRepository;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.DataUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import static com.bogdanorzea.popularmovies.utility.DataUtils.formatDuration;
import static com.bogdanorzea.popularmovies.utility.DataUtils.formatMoney;


public class DescriptionTab extends Fragment {
    private AVLoadingIndicatorView mAvi;
    private AsyncTaskUtils.RequestTaskListener<Movie> mRequestTaskListener =
            new AsyncTaskUtils.RequestTaskListener<Movie>() {
                @Override
                public void onTaskStarting() {
                    showProgress();
                }

                @Override
                public void onTaskComplete(Movie movie) {
                    MovieRepository<Movie> repository = new MovieSQLiteRepository(getContext());
                    repository.update(movie);

                    hideProgress();
                    displayDescription(movie.id);
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_description, container, false);
        mAvi = view.findViewById(R.id.avi);

        if (getArguments() != null) {
            ScrollView scrollView = view.findViewById(R.id.scroll_view);
            ViewCompat.setNestedScrollingEnabled(scrollView, true);

            int movieId = getArguments().getInt("movie_id");

            if (NetworkUtils.hasInternetConnection(getContext())) {
                new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, Movie.class)
                        .execute(NetworkUtils.movieDetailsUrl(movieId));
            } else {
                displayDescription(movieId);
            }
        }

        return view;
    }

    private void displayDescription(int movieId) {
        MovieRepository<Movie> repository = new MovieSQLiteRepository(getContext());
        View view = getView();
        if (view == null) {
            return;
        }

        Movie movie = repository.get(movieId);

        if (movie != null) {
            // Poster
            ImageView poster = view.findViewById(R.id.poster);
            byte[] image = movie.image;
            poster.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

            // Release date
            ((TextView) view.findViewById(R.id.release_date))
                    .setText(DataUtils.getYear(movie.releaseDate));

            // Title
            ((TextView) view.findViewById(R.id.title)).setText(movie.title);

            // Tagline
            ((TextView) view.findViewById(R.id.tagline)).setText(
                    DataUtils.quoteString(movie.tagline));

            // Overview
            ((TextView) view.findViewById(R.id.overview)).setText(movie.overview);

            // Score
            ((RatingBar) view.findViewById(R.id.score))
                    .setRating((float) movie.voteAverage / 2);

            // Runtime
            ((TextView) view.findViewById(R.id.runtime)).setText(formatDuration(movie.runtime));

            // Budget
            ((TextView) view.findViewById(R.id.budget)).setText(formatMoney(movie.budget));

            // Revenue
            ((TextView) view.findViewById(R.id.revenue)).setText(formatMoney(movie.revenue));

            // Genre
            ((TextView) view.findViewById(R.id.genre)).setText(DataUtils.printGenres(movie.genres));

        }
    }

    private void showProgress() {
        mAvi.smoothToShow();
    }

    private void hideProgress() {
        mAvi.smoothToHide();
    }
}
