package com.bogdanorzea.popularmovies.ui;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.MovieCategoryPagerAdapter;
import com.bogdanorzea.popularmovies.data.MovieRepository;
import com.bogdanorzea.popularmovies.data.MovieSQLiteRepository;
import com.bogdanorzea.popularmovies.fragment.CastTab;
import com.bogdanorzea.popularmovies.fragment.DescriptionTab;
import com.bogdanorzea.popularmovies.fragment.ReviewsTab;
import com.bogdanorzea.popularmovies.fragment.VideosTab;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.FragmentUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ID_INTENT_KEY = "movie_id";
    private final MovieRepository<Movie> repository = new MovieSQLiteRepository(this);
    private int mMovieId;
    private AppBarLayout mAppBarLayout;
    private AsyncTaskUtils.RequestTaskListener<Movie> mRequestTaskListener =
            new AsyncTaskUtils.RequestTaskListener<Movie>() {
                @Override
                public void onTaskStarting() {
                    showProgress();
                }

                @Override
                public void onTaskComplete(Movie movie) {
                    repository.update(movie);

                    hideProgress();
                    loadBackdropImage();
                    populateTabs();
                }
            };
    private Menu mMenu;

    public static void changeFavoriteMenuItemResource(Menu menu, int imageResource) {
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

        setBackdropColorFilter();

        mAppBarLayout = findViewById(R.id.app_bar);
        handleIntent();

    }

    private void populateTabs() {
        ViewPager viewPager = findViewById(R.id.tab_viewpager);
        MovieCategoryPagerAdapter pagerAdapter = new MovieCategoryPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovieId, DescriptionTab.class), "DESCRIPTION");
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovieId, VideosTab.class), "VIDEOS");
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovieId, CastTab.class), "CAST");
        pagerAdapter.addFragment(FragmentUtils.buildFragment(mMovieId, ReviewsTab.class), "REVIEWS");

        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.movie_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (repository.isFavorite(mMovieId)) {
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
                toggleMovieFavoriteStatus();
                return true;
            case R.id.action_homepage:
                openMovieHomepage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleMovieFavoriteStatus() {
        if (repository.isFavorite(mMovieId)) {
            repository.setFavorite(mMovieId, false);
            changeFavoriteMenuItemResource(mMenu, R.drawable.ic_favorite_border_white_24dp);
        } else {
            repository.setFavorite(mMovieId, true);
            changeFavoriteMenuItemResource(mMenu, R.drawable.ic_favorite_white_24dp);
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mMovieId = intent.getIntExtra(MOVIE_ID_INTENT_KEY, -1);
//            if (NetworkUtils.hasInternetConnection(this)) {
//                if (mMovieId != -1) {
//                    new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, Movie.class)
//                            .execute(NetworkUtils.movieDetailsUrl(mMovieId));
//                }
//            } else {
//                Toast.makeText(this, "In offline mode, there is limited information available.", Toast.LENGTH_SHORT).show();
//                populateTabs();
//            }
            populateTabs();
        }
    }

    private void setBackdropColorFilter() {
        ImageView backdropImage = findViewById(R.id.movie_backdrop);
        backdropImage.setColorFilter(
                new PorterDuffColorFilter(getResources().getColor(
                        R.color.colorPrimary), PorterDuff.Mode.LIGHTEN));
    }

    private void showProgress() {
        //mProgressBar.setVisibility(View.VISIBLE);
        mAppBarLayout.setExpanded(false);
    }

    private void hideProgress() {
        //mProgressBar.setVisibility(View.GONE);
        mAppBarLayout.setExpanded(true);
    }

    private void openMovieHomepage() {
        Movie movie = repository.get(mMovieId);
        if (!TextUtils.isEmpty(movie.homepage)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(movie.homepage));
        } else {
            Toast.makeText(this, "Couldn't launch the movie's homepage", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBackdropImage() {
        Movie movie = repository.get(mMovieId);

        if (movie != null) {
            ImageView backdrop = findViewById(R.id.movie_backdrop);
            NetworkUtils.loadBackdrop(this, backdrop, movie.backdropPath);
        }
    }
}
