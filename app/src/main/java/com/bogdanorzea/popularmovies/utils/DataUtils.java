package com.bogdanorzea.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.bogdanorzea.popularmovies.R;

public class DataUtils {
    public static String getPreferredSortRule(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        String sortAttribute = sharedPreferences.getString(
                context.getString(R.string.pref_sort_by),
                context.getString(R.string.pref_sort_by_popularity));

        String sortOrder = sharedPreferences.getString(
                context.getString(R.string.pref_sort_order),
                context.getString(R.string.pref_sort_order_desc));
        return String.format("%s.%s",sortAttribute, sortOrder);
    }
}
