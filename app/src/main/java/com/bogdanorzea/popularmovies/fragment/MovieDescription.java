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
import com.bogdanorzea.popularmovies.utility.DataUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;


public class MovieDescription extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_description_layout, container, false);

        if (getArguments() != null) {
            ScrollView scrollView = view.findViewById(R.id.scroll_view);
            ViewCompat.setNestedScrollingEnabled(scrollView, true);

            Movie movie = getArguments().getParcelable("movie");
            if (movie != null) {
                // Poster
                ImageView poster = view.findViewById(R.id.movie_cover);
                NetworkUtils.loadPoster(getActivity(), poster, movie.posterPath);

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
            }
        }

        return view;
    }

}
