package com.bogdanorzea.popularmovies.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.bogdanorzea.popularmovies.data.RepositoryMovie;
import com.bogdanorzea.popularmovies.data.RepositoryMovieSQLite;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.DataUtils;

import static com.bogdanorzea.popularmovies.utility.DataUtils.formatDuration;
import static com.bogdanorzea.popularmovies.utility.DataUtils.formatMoney;


public class DescriptionTab extends Fragment {
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

    @Override
    public void onResume() {
        super.onResume();
        if (mMovieId != -1) {
            displayDescription(mMovieId);
        }
    }

    private void displayDescription(int movieId) {
        RepositoryMovie<Movie> repository = new RepositoryMovieSQLite(getContext());
        View view = getView();
        if (view == null) {
            return;
        }

        Movie movie = repository.get(movieId);

        if (movie != null) {
            // Poster
            ImageView poster = view.findViewById(R.id.poster);
            byte[] image = movie.posterImage;
            poster.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

            // Release date
            ((TextView) view.findViewById(R.id.release_date))
                    .setText(DataUtils.addParenthesis(movie.getYear()));

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
            ((TextView) view.findViewById(R.id.genre)).setText(movie.printGenres());

        }
    }
}
