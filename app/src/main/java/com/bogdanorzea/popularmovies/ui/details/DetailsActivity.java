package com.bogdanorzea.popularmovies.ui.details;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.data.MovieMapper;
import com.bogdanorzea.popularmovies.data.MoviesContract;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.ui.PagerAdapter;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.FragmentUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String MOVIE_ID = "movie_id";
    private static final int FETCH_MOVIE_LOADER = 2;
    private Menu mMenu;
    private Movie mMovie;

    private static void changeFavoriteMenuItemResource(Menu menu, int imageResource) {
        MenuItem menuItemFavorite = menu.findItem(R.id.action_favorite);
        menuItemFavorite.setIcon(imageResource);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        setBackdropColorFilter();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle args = new Bundle();
            int movieId = intent.getIntExtra(MOVIE_ID, -1);
            args.putInt(MOVIE_ID, movieId);
            getSupportLoaderManager().initLoader(FETCH_MOVIE_LOADER, args, this);

            refreshMovieInformation(movieId);
            populateTabs(movieId);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);

        if (mMovie != null && mMovie.isFavorite()) {
            changeFavoriteMenuItemResource(menu, R.drawable.ic_favorite_white_24dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                toggleMovieFavoriteStatus();
                return true;
            case R.id.action_homepage:
                openMovieHomepage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        int movieId = args.getInt(MOVIE_ID);
        Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));

        return new CursorLoader(this, movieUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mMovie = MovieMapper.fromCursor(data);

            loadBackdropImage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader = null;
    }

    private void populateTabs(int movieId) {
        ViewPager viewPager = findViewById(R.id.tab_viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(FragmentUtils.buildFragment(movieId, DescriptionTab.class), getString(R.string.description_tab_name));
        pagerAdapter.addFragment(FragmentUtils.buildFragment(movieId, VideosTab.class), getString(R.string.videos_tab_name));
        pagerAdapter.addFragment(FragmentUtils.buildFragment(movieId, CastTab.class), getString(R.string.cast_tab_name));
        pagerAdapter.addFragment(FragmentUtils.buildFragment(movieId, ReviewsTab.class), getString(R.string.reviews_tab_name));

        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.movie_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void toggleMovieFavoriteStatus() {
        if (mMovie != null) {
            Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(mMovie.id));
            if (mMovie.isFavorite()) {
                changeFavoriteMenuItemResource(mMenu, R.drawable.ic_favorite_border_white_24dp);
                mMovie.favorite = 0;
            } else {
                changeFavoriteMenuItemResource(mMenu, R.drawable.ic_favorite_white_24dp);
                mMovie.favorite = 1;
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE, mMovie.favorite);

            getContentResolver().update(movieUri, contentValues, null, null);
        }
    }

    private void refreshMovieInformation(int movieId) {
        if (NetworkUtils.hasInternetConnection(this)) {
            AsyncTaskUtils.RequestTaskListener<Movie> mRequestTaskListener =
                    new AsyncTaskUtils.RequestTaskListener<Movie>() {
                        @Override
                        public void onTaskStarting() {
                        }

                        @Override
                        public void onTaskComplete(Movie movie) {
                            Uri movieUri = Uri.withAppendedPath(MoviesContract.CONTENT_URI, String.valueOf(movieId));

                            ContentValues contentValues = MovieMapper.toContentValues(movie);
                            int rows = getContentResolver().update(movieUri, contentValues, null, null);

                            if (rows > 0)
                                Timber.d("Movie \"%s\" (%s) successfully updated", movie.title, movie.id);
                        }
                    };

            new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, Movie.class)
                    .execute(NetworkUtils.movieDetailsUrl(movieId));
        } else {
            Toast.makeText(this, getString(R.string.offline_limitation_warning), Toast.LENGTH_SHORT).show();
        }
    }

    private void setBackdropColorFilter() {
        ImageView backdropImage = findViewById(R.id.movie_backdrop);
        backdropImage.setColorFilter(
                new PorterDuffColorFilter(getResources().getColor(
                        R.color.colorPrimary), PorterDuff.Mode.LIGHTEN));
    }

    private void openMovieHomepage() {
        if (mMovie != null && !TextUtils.isEmpty(mMovie.homepage)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(DetailsActivity.this, Uri.parse(mMovie.homepage));
        } else {
            Toast.makeText(DetailsActivity.this, "Couldn't launch the movie's homepage", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBackdropImage() {
        ImageView backdrop = findViewById(R.id.movie_backdrop);
        if (mMovie != null) {
            Picasso.with(this)
                    .load(mMovie.getBackdropUrl())
                    .into(backdrop);
        }
    }
}
