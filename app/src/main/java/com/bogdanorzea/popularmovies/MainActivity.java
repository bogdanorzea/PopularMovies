package com.bogdanorzea.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.model.Movie;
import com.bogdanorzea.popularmovies.model.Popular;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);

        HttpUrl url = NetworkUtils.buildDiscoverUrl();
        AT at = new AT();
        at.execute(url);
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
            MyArrayAdapter myArrayAdapter = new MyArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, popular.results);
            lv.setAdapter(myArrayAdapter);
        }
    }

    private class MyArrayAdapter extends ArrayAdapter<Movie> {
        private final Context context;
        List<Movie> lst = null;

        public MyArrayAdapter(@NonNull Context context, int resource, List<Movie> results) {
            super(context, resource, results);
            lst = results;
            this.context = context;
        }


        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            TextView textView = rowView.findViewById(android.R.id.text1);
            textView.setText(lst.get(position).originalTitle);

            return rowView;
        }

    }
}
