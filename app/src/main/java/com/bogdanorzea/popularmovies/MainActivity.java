package com.bogdanorzea.popularmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.model.response.DiscoverResponse;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private CoverAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.lv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CoverAdapter(MainActivity.this, null);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    loadData();
                }
            }
        });

        if (NetworkUtils.hasInternetConnection(this)) {
            loadData();
        } else {
            TextView textView = (TextView) findViewById(R.id.warning_message);
            textView.setText(R.string.warning_no_internet);
            textView.setVisibility(View.VISIBLE);
        }
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
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSortingDialog() {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(new String[] {"ana", "are", "mere" }, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void loadData() {
        HttpUrl url = NetworkUtils.movieDiscoverUrl(mAdapter.nextPageToLoad);
        new DiscoverAsyncTask().execute(url);
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void loadMoreData() {
        mAdapter.notifyDataSetChanged();
    }

    private class DiscoverAsyncTask extends AsyncTask<HttpUrl, Void, DiscoverResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected DiscoverResponse doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<DiscoverResponse> jsonAdapter = moshi.adapter(DiscoverResponse.class);

            DiscoverResponse discoverResponse = null;

            try {
                discoverResponse = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return discoverResponse;
        }

        @Override
        protected void onPostExecute(DiscoverResponse discoverResponse) {
            if (mAdapter.movies == null) {
                mAdapter.movies = discoverResponse.results;
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.movies.addAll(discoverResponse.results);
            }

            hideProgress();
            mAdapter.notifyDataSetChanged();
            mAdapter.nextPageToLoad += 1;
        }
    }

}
