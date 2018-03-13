package com.bogdanorzea.popularmovies.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.MovieCategoryPagerAdapter;
import com.bogdanorzea.popularmovies.data.MoviesContract;
import com.bogdanorzea.popularmovies.data.MoviesContract.MovieEntry;
import com.bogdanorzea.popularmovies.fragment.MovieCast;
import com.bogdanorzea.popularmovies.fragment.MovieDescription;
import com.bogdanorzea.popularmovies.fragment.MovieReviews;
import com.bogdanorzea.popularmovies.fragment.MovieVideos;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.FragmentUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

import timber.log.Timber;

import static com.bogdanorzea.popularmovies.utility.NetworkUtils.posterFullPath;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ID_INTENT_KEY = "movie_id";
    public Movie mMovie;
    private int mMovieId;
    private ProgressBar mProgressBar;
    private AppBarLayout mAppBarLayout;
    private AsyncTaskUtils.RequestTaskListener<Movie> mRequestTaskListener =
            new AsyncTaskUtils.RequestTaskListener<Movie>() {
                @Override
                public void onTaskStarting() {
                    showProgress();
                }

                @Override
                public void onTaskComplete(Movie movie) {
                    mMovie = movie;
                    hideProgress();
                    loadBackdropImage();
                    populateTabs();
                }
            };
    private Menu mMenu;

    public static boolean isMovieFavorite(Context context, int movieId) {
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));
        Timber.d("Movie uri is %s", movieUri);
        boolean isFavorite = false;

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(movieUri, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int favoriteColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_FAVORITE);
                int favoriteValue = cursor.getInt(favoriteColumnIndex);

                if (favoriteValue == 1) {
                    isFavorite = true;
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return isFavorite;
    }

    public static void changeFavoriteMenuItemResource(Menu menu, int imageResource) {
        int actionFavorite = R.id.action_favorite;

        MenuItem menuItemFavorite = menu.findItem(actionFavorite);
        menuItemFavorite.setIcon(imageResource);
    }

    private static Movie getMovie(Context context, int movieId) {
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));

        Cursor cursor = null;
        Movie movie = new Movie();
        try {
            cursor = context.getContentResolver().query(movieUri, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int idColumnIndex = cursor.getColumnIndex(MovieEntry._ID);
                int titleColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE);
                int releaseDateColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_DATE);
                int taglineColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TAGLINE);
                int overviewColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW);
                int runtimeColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RUNTIME);
                int voteAverageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_AVERAGE);
                int voteCountColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_COUNT);
                int homepageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_HOMEPAGE);
                int backdropPathColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_BACKDROP_PATH);
                int posterPathColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_PATH);

                movie.id = cursor.getInt(idColumnIndex);
                movie.title = cursor.getString(titleColumnIndex);
                movie.releaseDate = cursor.getString(releaseDateColumnIndex);
                movie.tagline = cursor.getString(taglineColumnIndex);
                movie.overview = cursor.getString(overviewColumnIndex);
                movie.runtime = cursor.getInt(runtimeColumnIndex);
                movie.voteAverage = cursor.getDouble(voteAverageColumnIndex);
                movie.voteCount = cursor.getInt(voteCountColumnIndex);
                movie.homepage = cursor.getString(homepageColumnIndex);
                movie.backdropPath = cursor.getString(backdropPathColumnIndex);
                movie.posterPath = cursor.getString(posterPathColumnIndex);

            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return movie;

    }

    private static Uri saveMovie(Context context, Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry._ID, movie.id);
        values.put(MovieEntry.COLUMN_NAME_TITLE, movie.title);
        values.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.releaseDate);
        values.put(MovieEntry.COLUMN_NAME_TAGLINE, movie.tagline);
        values.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.overview);
        values.put(MovieEntry.COLUMN_NAME_RUNTIME, movie.runtime);
        values.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.voteAverage);
        values.put(MovieEntry.COLUMN_NAME_VOTE_COUNT, movie.voteCount);
        values.put(MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.backdropPath);
        values.put(MovieEntry.COLUMN_NAME_HOMEPAGE, movie.homepage);
        values.put(MovieEntry.COLUMN_NAME_POSTER_PATH, movie.posterPath);
        values.put(MovieEntry.COLUMN_NAME_POSTER_IMAGE,
                NetworkUtils.getImageBytes(context, posterFullPath(movie.posterPath)));

        return context.getContentResolver().insert(MoviesContract.CONTENT_URI, values);
    }

    private static int setFavoriteStatus(Context context, Movie movie, boolean isFavorite) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry._ID, movie.id);
        values.put(MovieEntry.COLUMN_NAME_FAVORITE, isFavorite ? 1 : 0);

        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movie.id));
        return context.getContentResolver().update(movieUri, values, null, null);
    }

    private static int removeMovie(Context context, int movieId) {
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));
        return context.getContentResolver().delete(movieUri, null, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setBackdropColorFilter();

        mProgressBar = findViewById(R.id.progressBar);
        mAppBarLayout = findViewById(R.id.app_bar);

        handleIntent();

    }

    private void populateTabs() {
        ViewPager viewPager = findViewById(R.id.tab_viewpager);
        MovieCategoryPagerAdapter pagerAdapter = new MovieCategoryPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovie, MovieDescription.class), "DESCRIPTION");
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovie.id, MovieVideos.class), "VIDEOS");
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovie.id, MovieCast.class), "CAST");
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovie.id, MovieReviews.class), "REVIEWS");

        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.movie_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isMovieFavorite(this, mMovieId)) {
            changeFavoriteMenuItemResource(menu, R.drawable.ic_favorite_white_24dp);
        } else {
            changeFavoriteMenuItemResource(menu, R.drawable.ic_favorite_border_white_24dp);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                toggleMovieFavoriteStatus(mMovie);
                return true;
            case R.id.action_cache:
                mMovie = getMovie(this, mMovieId);
                populateTabs();
                return true;
            case R.id.action_homepage:
                openMovieHomepage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleMovieFavoriteStatus(Movie movie) {
        Context context = this;
        int movieId = movie.id;

        if (isMovieFavorite(context, movieId)) {
            int rowsAffected = setFavoriteStatus(this, movie, false);

            if (1 == rowsAffected) {
                changeFavoriteMenuItemResource(mMenu, R.drawable.ic_favorite_border_white_24dp);
            } else {
                Timber.e("Failed to remove the movie with id %s", movieId);
            }

        } else {
            Uri newRowID = saveMovie(context, movie);

            if (newRowID != null) {
                int movieUri = setFavoriteStatus(this, movie, true);
                if (movieUri == 1) {
                    changeFavoriteMenuItemResource(mMenu, R.drawable.ic_favorite_white_24dp);
                }
            } else {
                Timber.e("Failed to add to favorites the movie with id %s", movieId);
            }
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (NetworkUtils.hasInternetConnection(this)) {
                mMovieId = intent.getIntExtra(MOVIE_ID_INTENT_KEY, -1);
                if (mMovieId != -1) {
                    new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, Movie.class)
                            .execute(NetworkUtils.movieDetailsUrl(mMovieId));
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
        mAppBarLayout.setExpanded(false);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mAppBarLayout.setExpanded(true);
    }

    private void openMovieHomepage() {
        if (mMovie != null && !TextUtils.isEmpty(mMovie.homepage)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(mMovie.homepage));

        } else {
            Toast.makeText(this, "Couldn't launch the movie's homepage", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBackdropImage() {
        ImageView backdrop = findViewById(R.id.movie_backdrop);
        NetworkUtils.loadBackdrop(this, backdrop, mMovie.backdropPath);
    }
}
