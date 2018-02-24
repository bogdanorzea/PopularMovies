package com.bogdanorzea.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bogdanorzea.popularmovies.model.response.Discover;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.lv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(null);

        HttpUrl url = NetworkUtils.movieDiscoverUrl();
        new AT().execute(url);
    }

    private class AT extends AsyncTask<HttpUrl, Void, Discover> {

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
            CoverAdapter coverAdapter = new CoverAdapter(MainActivity.this, discover.results);
            mRecyclerView.setAdapter(coverAdapter);
        }
    }

}
