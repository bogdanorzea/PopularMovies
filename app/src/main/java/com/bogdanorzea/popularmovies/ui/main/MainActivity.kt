package com.bogdanorzea.popularmovies.ui.main

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem

import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.ui.PagerAdapter
import com.bogdanorzea.popularmovies.ui.search.SearchActivity
import com.bogdanorzea.popularmovies.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(FavoritesTab(), getString(R.string.favorites_tab_name))
        pagerAdapter.addFragment(PopularTab(), getString(R.string.popular_tab_name))
        pagerAdapter.addFragment(TopRatedTab(), getString(R.string.top_rated_tab_name))

        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2

        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(this, SearchActivity::class.java)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val startSettingsActivity = Intent(this, SettingsActivity::class.java)
                startActivity(startSettingsActivity)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
