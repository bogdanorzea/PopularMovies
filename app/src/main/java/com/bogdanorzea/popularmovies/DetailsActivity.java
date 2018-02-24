package com.bogdanorzea.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.model.Movie;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.HttpUrl;

public class DetailsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private Movie mMovie;
    private ConstraintLayout constraintLayout;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        constraintLayout = findViewById(R.id.constraint_layout);
        appBarLayout = findViewById(R.id.app_bar);
        //toolbar.setTitle("");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        // TODO add checks fot intent validity
        int movieId = intent.getIntExtra("movie_id", -1);

        String movieName = intent.getStringExtra("movie_title");
        getSupportActionBar().setTitle(movieName);

        if (movieId != -1) {
            new AT().execute(NetworkUtils.buildMovieUrl(movieId));
        }
    }

    private void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.INVISIBLE);
        appBarLayout.setExpanded(false);
    }

    private void hideProgress(){
        appBarLayout.setExpanded(true);
        progressBar.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);
    }

    private void displayMovie() {

        ((TextView) findViewById(R.id.movie_title)).setText(mMovie.title);
        ((TextView) findViewById(R.id.movie_tagline)).setText(mMovie.tagline);
        ((TextView) findViewById(R.id.movie_website)).setText(mMovie.homepage);
        ((TextView) findViewById(R.id.movie_release_date)).setText(mMovie.releaseDate);
        ((TextView) findViewById(R.id.movie_overview)).setText(mMovie.overview);

        ImageView poster = findViewById(R.id.movie_poster);

        NetworkUtils.loadImage(this, poster, mMovie.backdropPath);
    }

    private class AT extends AsyncTask<HttpUrl, Void, Movie> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Movie doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Movie> jsonAdapter = moshi.adapter(Movie.class);

            Movie movie = null;

            try {
                movie = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            mMovie = movie;
            hideProgress();
            displayMovie();
        }
    }
}
