package com.bogdanorzea.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utils.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utils.DataUtils;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;

import static com.bogdanorzea.popularmovies.utils.DataUtils.formatDuration;
import static com.bogdanorzea.popularmovies.utils.DataUtils.formatMoney;


public class MovieFactsFragment extends Fragment {
    private int movieId;
    private Movie mCurrentMovie;

    private AsyncTaskUtils.AsyncTaskListener<Movie> mMovieAsyncTaskListener =
            new AsyncTaskUtils.AsyncTaskListener<Movie>() {
                @Override
                public void onTaskStarting() {
                    //showProgress();
                }

                @Override
                public void onTaskComplete(Movie movie) {
                    mCurrentMovie = movie;
                    //hideProgress();
                    displayMovieDetails();
                }
            };
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.movie_facts_layout, container, false);

        Bundle arguments = getArguments();
        movieId = arguments.getInt("movie_id");

        new AsyncTaskUtils.MovieDetailsAsyncTask(mMovieAsyncTaskListener)
                .execute(NetworkUtils.movieDetailsUrl(movieId));

        return view;
    }

    private void displayMovieDetails() {
        Context context = getActivity();

        // TODO move this to DetailsActivity
        //ImageView backdrop = view.findViewById(R.id.movie_backdrop);
        //NetworkUtils.loadBackdrop(context, backdrop, mCurrentMovie.backdropPath);

        ImageView poster = view.findViewById(R.id.movie_cover);
        NetworkUtils.loadPoster(context, poster, mCurrentMovie.posterPath);

        ((TextView) view.findViewById(R.id.movie_release_date)).setText(
                String.format("(%s)", mCurrentMovie.releaseDate.substring(0, 4)));
        ((TextView) view.findViewById(R.id.movie_title)).setText(mCurrentMovie.title);
        ((TextView) view.findViewById(R.id.movie_tagline)).setText(
                DataUtils.quoteString(mCurrentMovie.tagline));
        ((TextView) view.findViewById(R.id.movie_overview)).setText(mCurrentMovie.overview);
        ((TextView) view.findViewById(R.id.movie_runtime)).setText(formatDuration(mCurrentMovie.runtime));
        ((TextView) view.findViewById(R.id.movie_budget)).setText(formatMoney(mCurrentMovie.budget));
        ((TextView) view.findViewById(R.id.movie_revenue)).setText(formatMoney(mCurrentMovie.revenue));
        ((TextView) view.findViewById(R.id.movie_genre)).setText(DataUtils.printGenres(mCurrentMovie.genres));

        ((RatingBar) view.findViewById(R.id.movie_score)).setRating(mCurrentMovie.voteAverage / 2);
    }
}
