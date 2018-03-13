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
import com.bogdanorzea.popularmovies.utility.DataUtils;

import static com.bogdanorzea.popularmovies.utility.DataUtils.formatDuration;
import static com.bogdanorzea.popularmovies.utility.DataUtils.formatMoney;


public class MovieDescription extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_description_layout, container, false);

        if (getArguments() != null) {
            ScrollView scrollView = view.findViewById(R.id.scroll_view);
            ViewCompat.setNestedScrollingEnabled(scrollView, true);

            MovieRepository<Movie> repository = new MovieSQLiteRepository(getContext());
            int movieId = getArguments().getInt("movie_id");
            Movie movie = repository.get(movieId);

            if (movie != null) {
                // Poster
                ImageView poster = view.findViewById(R.id.movie_cover);
                byte[] image = movie.image;
                poster.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

                // Release date
                ((TextView) view.findViewById(R.id.movie_release_date))
                        .setText(DataUtils.getYear(movie.releaseDate));

                // Title
                ((TextView) view.findViewById(R.id.movie_title)).setText(movie.title);

                // Tagline
                ((TextView) view.findViewById(R.id.movie_tagline)).setText(
                        DataUtils.quoteString(movie.tagline));

                // Overview
                ((TextView) view.findViewById(R.id.movie_overview)).setText(movie.overview);

                // Score
                ((RatingBar) view.findViewById(R.id.movie_score))
                        .setRating((float) movie.voteAverage / 2);

                // Runtime
                ((TextView) view.findViewById(R.id.movie_runtime)).setText(formatDuration(movie.runtime));

                // Budget
                ((TextView) view.findViewById(R.id.movie_budget)).setText(formatMoney(movie.budget));

                // Revenue
                ((TextView) view.findViewById(R.id.movie_revenue)).setText(formatMoney(movie.revenue));

                // Genre
                ((TextView) view.findViewById(R.id.movie_genre)).setText(DataUtils.printGenres(movie.genres));

            }
        }

        return view;
    }

}
