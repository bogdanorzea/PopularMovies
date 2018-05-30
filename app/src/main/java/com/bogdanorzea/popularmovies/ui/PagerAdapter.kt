package com.bogdanorzea.popularmovies.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val mFragmentList = mutableListOf<Fragment>()
    private val mTitleList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mTitleList.add(title)
    }
}
