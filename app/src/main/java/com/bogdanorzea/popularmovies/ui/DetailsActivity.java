package com.bogdanorzea.popularmovies.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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
import com.bogdanorzea.popularmovies.fragment.MovieCast;
import com.bogdanorzea.popularmovies.fragment.MovieDescription;
import com.bogdanorzea.popularmovies.fragment.MovieFacts;
import com.bogdanorzea.popularmovies.fragment.MovieReviews;
import com.bogdanorzea.popularmovies.fragment.MovieVideos;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ID_INTENT_KEY = "movie_id";
    public Movie mCurrentMovie;
    private ProgressBar mProgressBar;
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
                    loadBackdropImage();
                    populateTabs();
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
        mAppBarLayout = findViewById(R.id.app_bar);

        handleIntent();

    }

    private void populateTabs() {
        ViewPager viewPager = findViewById(R.id.tab_viewpager);
        MovieCategoryPagerAdapter pagerAdapter = new MovieCategoryPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(buildMovieDescription(mCurrentMovie), "DESCRIPTION");
        pagerAdapter.addFragment(buildMovieFacts(mCurrentMovie), "FACTS");
        pagerAdapter.addFragment(buildMovieVideos(mCurrentMovie), "VIDEOS");
        pagerAdapter.addFragment(buildMovieCast(mCurrentMovie), "CAST");
        pagerAdapter.addFragment(buildMovieReviews(mCurrentMovie), "REVIEWS");

        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private MovieDescription buildMovieDescription(Movie movie) {
        MovieDescription description = new MovieDescription();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        description.setArguments(bundle);

        return description;
    }

    private MovieFacts buildMovieFacts(Movie movie) {
        MovieFacts facts = new MovieFacts();

        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        facts.setArguments(bundle);

        return facts;
    }

    private MovieReviews buildMovieReviews(Movie movie) {
        MovieReviews reviews = new MovieReviews();

        Bundle bundle = new Bundle();
        bundle.putInt("movie_id", movie.id);
        reviews.setArguments(bundle);

        return reviews;
    }

    private MovieVideos buildMovieVideos(Movie movie) {
        MovieVideos videos = new MovieVideos();

        Bundle bundle = new Bundle();
        bundle.putInt("movie_id", movie.id);
        videos.setArguments(bundle);

        return videos;
    }

    private MovieCast buildMovieCast(Movie movie) {
        MovieCast cast = new MovieCast();

        Bundle bundle = new Bundle();
        bundle.putInt("movie_id", movie.id);
        cast.setArguments(bundle);

        return cast;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int actionTrailerId = 200;
        int actionWebsiteId = 300;

//        if (mCurrentMovie != null) {
//            if (!TextUtils.isEmpty(mCurrentMovie.homepage) &&
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
                    new AsyncTaskUtils.MovieDetailsAsyncTask(mMovieAsyncTaskListener)
                            .execute(NetworkUtils.movieDetailsUrl(movieId));
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

    private void addToFavorites() {
        Toast.makeText(this, "Will be added soon", Toast.LENGTH_SHORT).show();
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
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrentMovie.homepage));
//        startActivity(intent);
//    }

    private void loadBackdropImage() {
        ImageView backdrop = findViewById(R.id.movie_backdrop);
        NetworkUtils.loadBackdrop(this, backdrop, mCurrentMovie.backdropPath);
    }
}
