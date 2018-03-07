package com.bogdanorzea.popularmovies;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.model.object.Video;
import com.bogdanorzea.popularmovies.model.response.ReviewsResponse;
import com.bogdanorzea.popularmovies.model.response.VideosResponse;
import com.bogdanorzea.popularmovies.utils.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utils.DataUtils;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;

import static com.bogdanorzea.popularmovies.utils.DataUtils.formatDuration;
import static com.bogdanorzea.popularmovies.utils.DataUtils.formatMoney;

public class DetailsActivity extends AppCompatActivity {

    static final String MOVIE_ID_INTENT_KEY = "movie_id";

    private ProgressBar mProgressBar;
    private Movie mCurrentMovie;
    private VideosResponse mCurrentVideosResponse;
    private CardView mMovieCardView;
    private AppBarLayout mAppBarLayout;

    private AsyncTaskUtils.AsyncTaskListener<Movie> mMovieAsyncTaskListener =
            new AsyncTaskUtils.AsyncTaskListener<Movie>() {
                @Override
                public void onTaskStarting() {
                    showProgress();
                }

                @Override
                public void onTaskComplete(Movie movie) {
                    mCurrentMovie = movie;
                    hideProgress();
                    displayMovieDetails();
                }
            };
    private AsyncTaskUtils.AsyncTaskListener<VideosResponse> mMovieVideosAsyncTaskListener =
            new AsyncTaskUtils.AsyncTaskListener<VideosResponse>() {
                @Override
                public void onTaskStarting() {
                }

                @Override
                public void onTaskComplete(VideosResponse videosResponse) {
                    mCurrentVideosResponse = videosResponse;
                }
            };
    private AsyncTaskUtils.AsyncTaskListener<ReviewsResponse> mMovieReviewsAsyncTaskListener =
            new AsyncTaskUtils.AsyncTaskListener<ReviewsResponse>() {
                @Override
                public void onTaskStarting() {

                }

                @Override
                public void onTaskComplete(ReviewsResponse result) {
                    displayMovieReviews(result);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setBackdropColorFilter();

        mProgressBar = findViewById(R.id.progressBar);
        mMovieCardView = findViewById(R.id.movie_card);
        mAppBarLayout = findViewById(R.id.app_bar);

        handleIntent();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int actionTrailerId = 200;
        int actionWebsiteId = 300;

        if (mCurrentMovie != null) {
            if (!TextUtils.isEmpty(mCurrentMovie.homepage) &&
                    menu.findItem(actionWebsiteId) == null) {

                MenuItem item = menu.add(
                        Menu.NONE,
                        actionWebsiteId,
                        actionWebsiteId,
                        R.string.action_website);

                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                item.setOnMenuItemClickListener(menuItem -> {
                    openMovieWebsite();
                    return true;
                });
            }
        }

        if (mCurrentVideosResponse != null) {
            if (mCurrentVideosResponse.results.size() > 0 && menu.findItem(actionTrailerId) == null) {

                MenuItem item = menu.add(
                        Menu.NONE,
                        actionTrailerId,
                        actionTrailerId,
                        R.string.action_trailer);

                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                item.setOnMenuItemClickListener(menuItem -> {
                    openMovieTrailer();
                    return true;
                });
            }
        }

        return super.onPrepareOptionsMenu(menu);
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
            case R.id.action_favorite:
                addToFavorites();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (NetworkUtils.hasInternetConnection(this)) {
                int movieId = intent.getIntExtra(MOVIE_ID_INTENT_KEY, -1);
                if (movieId != -1) {
                    new AsyncTaskUtils.MovieDetailsAsyncTask(mMovieAsyncTaskListener).execute(NetworkUtils.movieDetailsUrl(movieId));
                    new AsyncTaskUtils.MovieVideosAsyncTask(mMovieVideosAsyncTaskListener).execute(NetworkUtils.movieVideosUrl(movieId));
                    new AsyncTaskUtils.MovieReviewsAsyncTask(mMovieReviewsAsyncTaskListener).execute(NetworkUtils.movieReviewsUrl(movieId, 1));
                }
            } else {
                Toast.makeText(this, R.string.warning_no_internet, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setBackdropColorFilter() {
        ImageView backdropImage = findViewById(R.id.movie_backdrop);
        backdropImage.setColorFilter(
                new PorterDuffColorFilter(getResources().getColor(
                        R.color.colorPrimary), PorterDuff.Mode.LIGHTEN));
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mMovieCardView.setVisibility(View.INVISIBLE);
        mAppBarLayout.setExpanded(false);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mMovieCardView.setVisibility(View.VISIBLE);
        mAppBarLayout.setExpanded(true);
    }

    private void addToFavorites() {
        Toast.makeText(this, "Will be added soon", Toast.LENGTH_SHORT).show();
    }

    private void openMovieTrailer() {
        for (int i = 0; i < mCurrentVideosResponse.results.size(); i++) {
            Video result = mCurrentVideosResponse.results.get(i);
            if (result.site.equalsIgnoreCase("YouTube") &&
                    result.type.equalsIgnoreCase("trailer")) {
                String youtubeLink = "https://www.youtube.com/watch?v=" + result.key;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
                startActivity(intent);
                return;
            }
        }
    }

    private void openMovieWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrentMovie.homepage));
        startActivity(intent);
    }

    private void displayMovieDetails() {
        ImageView backdrop = findViewById(R.id.movie_backdrop);
        NetworkUtils.loadBackdrop(this, backdrop, mCurrentMovie.backdropPath);

        ImageView poster = findViewById(R.id.movie_cover);
        NetworkUtils.loadPoster(this, poster, mCurrentMovie.posterPath);

        ((TextView) findViewById(R.id.movie_release_date)).setText(
                String.format("(%s)", mCurrentMovie.releaseDate.substring(0, 4)));
        ((TextView) findViewById(R.id.movie_title)).setText(mCurrentMovie.title);
        ((TextView) findViewById(R.id.movie_tagline)).setText(
                DataUtils.quoteString(mCurrentMovie.tagline));
        ((TextView) findViewById(R.id.movie_overview)).setText(mCurrentMovie.overview);
        ((TextView) findViewById(R.id.movie_runtime)).setText(formatDuration(mCurrentMovie.runtime));
        ((TextView) findViewById(R.id.movie_budget)).setText(formatMoney(mCurrentMovie.budget));
        ((TextView) findViewById(R.id.movie_revenue)).setText(formatMoney(mCurrentMovie.revenue));
        ((TextView) findViewById(R.id.movie_genre)).setText(DataUtils.printGenres(mCurrentMovie.genres));

        ((RatingBar) findViewById(R.id.movie_score)).setRating(mCurrentMovie.voteAverage / 2);
    }

    private void displayMovieReviews(ReviewsResponse result) {
        ListView reviewsListView = findViewById(R.id.reviews);
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, result.results);

        reviewsListView.setAdapter(reviewsAdapter);
    }

}
