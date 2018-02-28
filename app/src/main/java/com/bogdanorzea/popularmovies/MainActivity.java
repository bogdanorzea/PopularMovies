package com.bogdanorzea.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.model.response.ListOfMoviesResponse;
import com.bogdanorzea.popularmovies.utils.DataUtils;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ProgressBar mProgressBar;
    private RecyclerView mCoverRecyclerView;
    private CoverAdapter mAdapter;

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

                if (!recyclerView.canScrollVertically(1)) {
                    loadData();
                }
            }
        });

        loadData();
    }

    private void showNoInternetWarning() {
        findViewById(R.id.warning_message).setVisibility(View.VISIBLE);
    }

    private void hideNoInternetWarning() {
        findViewById(R.id.warning_message).setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
                url = NetworkUtils.moviePopularUrl(mAdapter.nextPageToLoad);
            } else if (preferredSortRule.equals(getString(R.string.pref_sort_by_top_rated))) {
                url = NetworkUtils.movieTopRatedUrl(mAdapter.nextPageToLoad);
            } else {
                return;
            }

            new ListOfMoviesAsyncTask().execute(url);
        } else {
            hideProgress();
            if (mAdapter.movies == null || mAdapter.movies.isEmpty()) {
                showNoInternetWarning();
            } else {
                Toast.makeText(this, getString(R.string.warning_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
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

    private class ListOfMoviesAsyncTask extends AsyncTask<HttpUrl, Void, ListOfMoviesResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected ListOfMoviesResponse doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<ListOfMoviesResponse> jsonAdapter = moshi.adapter(ListOfMoviesResponse.class);

            ListOfMoviesResponse listOfMoviesResponse = null;

            try {
                listOfMoviesResponse = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return listOfMoviesResponse;
        }

        @Override
        protected void onPostExecute(ListOfMoviesResponse listOfMoviesResponse) {
            if (listOfMoviesResponse != null) {
                if (null == mAdapter.movies) {
                    mAdapter.movies = listOfMoviesResponse.results;
                    mCoverRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.movies.addAll(listOfMoviesResponse.results);
                }

                hideProgress();
                mAdapter.notifyDataSetChanged();
                mAdapter.nextPageToLoad += 1;
            }
        }
    }

}
