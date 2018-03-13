package com.bogdanorzea.popularmovies.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.CoverAdapter;
import com.bogdanorzea.popularmovies.model.response.MoviesResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.DataUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private ProgressBar mProgressBar;
    private RecyclerView mCoverRecyclerView;
    private CoverAdapter mAdapter;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);
        mCoverRecyclerView = findViewById(R.id.cover_rv);
        mCoverRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mCoverRecyclerView.setHasFixedSize(true);

        mAdapter = new CoverAdapter(MainActivity.this, null);

        mCoverRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    loadData();
                }
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        prepareSearchMenuItem(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                reloadData();
                return true;
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        reloadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            displaySearchResult(intent.getStringExtra(SearchManager.QUERY));
        } else {
            reloadData();
        }
    }

    private void showNoInternetWarning() {
        findViewById(R.id.warning_message).setVisibility(View.VISIBLE);
    }

    private void hideNoInternetWarning() {
        findViewById(R.id.warning_message).setVisibility(View.GONE);
    }

    private void reloadData() {
        mAdapter = new CoverAdapter(MainActivity.this, null);
        mCoverRecyclerView.setAdapter(null);
        loadData();
    }

    private void loadData() {
        if (NetworkUtils.hasInternetConnection(this)) {
            hideNoInternetWarning();
            String preferredSortRule = DataUtils.getPreferredSortRule(this);

            HttpUrl url;
            if (preferredSortRule.equals(getString(R.string.pref_sort_by_popularity))) {
                url = NetworkUtils.moviePopularUrl(mAdapter.getNextPageToLoad());
            } else if (preferredSortRule.equals(getString(R.string.pref_sort_by_top_rated))) {
                url = NetworkUtils.movieTopRatedUrl(mAdapter.getNextPageToLoad());
            } else {
                return;
            }

            AsyncTaskUtils.RequestTaskListener<MoviesResponse> listener =
                    new AsyncTaskUtils.RequestTaskListener<MoviesResponse>() {

                        @Override
                        public void onTaskStarting() {
                            showProgress();
                        }

                        @Override
                        public void onTaskComplete(MoviesResponse moviesResponse) {
                            if (moviesResponse != null) {
                                if (0 == mAdapter.getItemCount()) {
                                    mAdapter.setMovies(moviesResponse.results);
                                    mCoverRecyclerView.setAdapter(mAdapter);
                                } else {
                                    mAdapter.addMovies(moviesResponse.results);
                                }

                                hideProgress();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    };

            new AsyncTaskUtils.RequestTask<>(listener, MoviesResponse.class).execute(url);
        } else {
            hideProgress();
            if (mAdapter.getItemCount() == 0) {
                showNoInternetWarning();
            } else {
                Toast.makeText(this, getString(R.string.warning_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displaySearchResult(String query) {
        HttpUrl httpUrl = NetworkUtils.movieSearchUrl(query);
        AsyncTaskUtils.RequestTaskListener<MoviesResponse> listener = new AsyncTaskUtils.RequestTaskListener<MoviesResponse>() {
            @Override
            public void onTaskStarting() {
                showProgress();
            }

            @Override
            public void onTaskComplete(MoviesResponse moviesResponse) {
                if (moviesResponse != null) {
                    CoverAdapter adapter = new CoverAdapter(MainActivity.this, moviesResponse.results);

                    mCoverRecyclerView.setAdapter(null);
                    mCoverRecyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    hideProgress();
                }
            }
        };

        new AsyncTaskUtils.RequestTask<>(listener, MoviesResponse.class).execute(httpUrl);
    }

    private void showProgress() {
        isLoading = true;
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        isLoading = false;
        mProgressBar.setVisibility(View.GONE);
    }

    private void prepareSearchMenuItem(Menu menu) {
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                onNewIntent(null);
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }
}
