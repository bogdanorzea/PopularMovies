package com.bogdanorzea.popularmovies.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.MovieCategoryPagerAdapter;
import com.bogdanorzea.popularmovies.data.MoviesContract.MovieEntry;
import com.bogdanorzea.popularmovies.fragment.MovieCast;
import com.bogdanorzea.popularmovies.fragment.MovieDescription;
import com.bogdanorzea.popularmovies.fragment.MovieFacts;
import com.bogdanorzea.popularmovies.fragment.MovieReviews;
import com.bogdanorzea.popularmovies.fragment.MovieVideos;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.FragmentUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

import timber.log.Timber;

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
    private boolean isFavorite = false;

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
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovie, MovieFacts.class), "FACTS");

        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovie.id, MovieVideos.class), "VIDEOS");
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovie.id, MovieCast.class), "CAST");
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovie.id, MovieReviews.class), "REVIEWS");

        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int actionTrailerId = 200;
        int actionWebsiteId = 300;

        Uri movieUri = Uri.withAppendedPath(MovieEntry.CONTENT_URI, String.valueOf(mMovieId));
        Timber.d("Movie uri is %s", movieUri);

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(movieUri, null, null, null, null);
            if (cursor != null && cursor.getCount() == 1) {

                cursor.moveToFirst();
                int favoriteColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_FAVORITE);
                Timber.d("Favorite column index is %s", favoriteColumnIndex);

                int favoriteValue = cursor.getInt(favoriteColumnIndex);

                if (favoriteValue == 1) {
                    isFavorite = true;
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        if (isFavorite) {
            MenuItem menuItemFavorite = menu.findItem(R.id.action_favorite);
            menuItemFavorite.setIcon(R.drawable.ic_favorite_white_24dp);
        }


//        if (mMovie != null) {
//            if (!TextUtils.isEmpty(mMovie.homepage) &&
//                    menu.findItem(actionWebsiteId) == null) {
//
//                MenuItem item = menu.add(
//                        Menu.NONE,
//                        actionWebsiteId,
//                        actionWebsiteId,
//                        R.string.action_website);
//
//                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
//                item.setOnMenuItemClickListener(menuItem -> {
//                    openMovieWebsite();
//                    return true;
//                });
//            }
//        }
//
//        if (mCurrentVideosResponse != null) {
//            if (mCurrentVideosResponse.results.size() > 0 && menu.findItem(actionTrailerId) == null) {
//
//                MenuItem item = menu.add(
//                        Menu.NONE,
//                        actionTrailerId,
//                        actionTrailerId,
//                        R.string.action_trailer);
//
//                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
//                item.setOnMenuItemClickListener(menuItem -> {
//                    openMovieTrailer();
//                    return true;
//                });
//            }
//        }

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
                addMovie();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void addMovie() {
        ContentValues values = new ContentValues();
        values.put(MovieEntry._ID, mMovie.id);
        values.put(MovieEntry.COLUMN_NAME_TITLE, mMovie.title);
        values.put(MovieEntry.COLUMN_NAME_FAVORITE, 1);

        Uri newRowID = getContentResolver().insert(MovieEntry.CONTENT_URI, values);

        if (newRowID != null) {
            Toast.makeText(this, "Successfully added movie " + mMovie.title + " to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Timber.e("Failed to add to favorites the movie with id %s", mMovie.id);
        }
    }

//    private void openMovieTrailer() {
//        for (int i = 0; i < mCurrentVideosResponse.results.size(); i++) {
//            Video result = mCurrentVideosResponse.results.get(i);
//            if (result.site.equalsIgnoreCase("YouTube") &&
//                    result.type.equalsIgnoreCase("trailer")) {
//                String youtubeLink = "https://www.youtube.com/watch?v=" + result.key;
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
//                startActivity(intent);
//                return;
//            }
//        }
//    }
//
//    private void openMovieWebsite() {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mMovie.homepage));
//        startActivity(intent);
//    }

    private void loadBackdropImage() {
        ImageView backdrop = findViewById(R.id.movie_backdrop);
        NetworkUtils.loadBackdrop(this, backdrop, mMovie.backdropPath);
    }
}
