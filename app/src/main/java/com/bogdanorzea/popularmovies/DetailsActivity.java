package com.bogdanorzea.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.model.objects.Movie;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.HttpUrl;

public class DetailsActivity extends AppCompatActivity {

    static final String MOVIE_ID_INTENT_KEY = "movie_id";
    static final String MOVIE_TITLE_INTENT_KEY = "movie_title";

    ProgressBar mProgressBar;
    private Movie mCurrentMovie;
    private ConstraintLayout mConstraintLayout;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar = findViewById(R.id.progressBar);
        mConstraintLayout = findViewById(R.id.constraint_layout);
        mAppBarLayout = findViewById(R.id.app_bar);

        Intent intent = getIntent();
        if (NetworkUtils.hasInternetConnection(this)) {
            String movieName = intent.getStringExtra(MOVIE_TITLE_INTENT_KEY);
            getSupportActionBar().setTitle(movieName);

            int movieId = intent.getIntExtra(MOVIE_ID_INTENT_KEY, -1);
            if (movieId != -1) {
                new AT().execute(NetworkUtils.movieDetailsUrl(movieId));
            }
        } else {
            Toast.makeText(this, R.string.warning_no_internet, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mConstraintLayout.setVisibility(View.INVISIBLE);
        mAppBarLayout.setExpanded(false);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mConstraintLayout.setVisibility(View.VISIBLE);
        mAppBarLayout.setExpanded(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_website:
                openMovieWebsite();
                return true;
            case R.id.action_trailer:
                openMovieTrailer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMovieTrailer() {
        Toast.makeText(this, "Will be added soon", Toast.LENGTH_SHORT).show();
    }

    private void openMovieWebsite() {
        if (!TextUtils.isEmpty(mCurrentMovie.homepage)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrentMovie.homepage));
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.warning_no_website, Toast.LENGTH_SHORT).show();
        }
    }

    private void renderMovieInformation() {
        ImageView poster = findViewById(R.id.movie_backdrop);
        NetworkUtils.loadImage(this, poster, mCurrentMovie.backdropPath);

        ((TextView) findViewById(R.id.movie_title)).setText(mCurrentMovie.title);
        ((TextView) findViewById(R.id.movie_tagline)).setText(mCurrentMovie.tagline);
        ((TextView) findViewById(R.id.movie_website)).setText(mCurrentMovie.homepage);
        ((TextView) findViewById(R.id.movie_release_date)).setText(mCurrentMovie.releaseDate);
        ((TextView) findViewById(R.id.movie_overview)).setText(mCurrentMovie.overview);
        ((RatingBar) findViewById(R.id.movie_score)).setRating(mCurrentMovie.voteAverage / 2);
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
            mCurrentMovie = movie;
            hideProgress();
            renderMovieInformation();
        }
    }
}
