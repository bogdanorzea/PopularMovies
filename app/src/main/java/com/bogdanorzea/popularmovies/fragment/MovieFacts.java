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

import static com.bogdanorzea.popularmovies.utility.DataUtils.formatDuration;
import static com.bogdanorzea.popularmovies.utility.DataUtils.formatMoney;


public class MovieFacts extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_facts_layout, container, false);

        if (getArguments() != null) {
            ScrollView scrollView = view.findViewById(R.id.scroll_view);
            ViewCompat.setNestedScrollingEnabled(scrollView, true);

            Movie movie = getArguments().getParcelable("movie");
            if (movie != null) {
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
