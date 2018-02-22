package com.bogdanorzea.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bogdanorzea.popularmovies.model.Popular;
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

        HttpUrl url = NetworkUtils.buildDiscoverUrl();
        new AT().execute(url);
    }

    private class AT extends AsyncTask<HttpUrl, Void, Popular> {

        @Override
        protected Popular doInBackground(HttpUrl... httpUrls) {
            String response = "";
            try {
                response = NetworkUtils.fetch(httpUrls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Popular> jsonAdapter = moshi.adapter(Popular.class);

            Popular popular = null;

            try {
                popular = jsonAdapter.fromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return popular;
        }

        @Override
        protected void onPostExecute(Popular popular) {
            CoverAdapter coverAdapter = new CoverAdapter(MainActivity.this, popular.results);
            mRecyclerView.setAdapter(coverAdapter);
        }
    }

}
