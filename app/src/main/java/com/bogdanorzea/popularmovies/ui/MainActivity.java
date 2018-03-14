package com.bogdanorzea.popularmovies.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.MovieCategoryPagerAdapter;
import com.bogdanorzea.popularmovies.fragment.MoviesFavorites;
import com.bogdanorzea.popularmovies.fragment.MoviesPopular;
import com.bogdanorzea.popularmovies.fragment.MoviesTopRated;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MovieCategoryPagerAdapter pagerAdapter = new MovieCategoryPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new MoviesFavorites(), "Favorites");
        pagerAdapter.addFragment(new MoviesPopular(), "Popular");
        pagerAdapter.addFragment(new MoviesTopRated(), "Top rated");

        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
