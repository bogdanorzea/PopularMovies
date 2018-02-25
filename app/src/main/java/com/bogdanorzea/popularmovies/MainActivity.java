package com.bogdanorzea.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.model.response.Discover;
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
        //mRecyclerView.setAdapter(null);
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

    private void loadData() {
        HttpUrl url = NetworkUtils.movieDiscoverUrl(mAdapter.nextPageToLoad);
        new AT().execute(url);
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        //mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        //mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadMoreData() {
        mAdapter.notifyDataSetChanged();
    }

    private class AT extends AsyncTask<HttpUrl, Void, Discover> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Discover doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Discover> jsonAdapter = moshi.adapter(Discover.class);

            Discover discover = null;

            try {
                discover = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return discover;
        }

        @Override
        protected void onPostExecute(Discover discover) {
            if (mAdapter.movies == null) {
                mAdapter.movies = discover.results;
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.movies.addAll(discover.results);
            }

            hideProgress();
            mAdapter.notifyDataSetChanged();
            mAdapter.nextPageToLoad += 1;
        }
    }

}
