package com.bogdanorzea.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.objects.Genre;

import java.text.DecimalFormat;
import java.util.List;

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
        return String.format("%s.%s", sortAttribute, sortOrder);
    }

    public static String formatMoney(long sum) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");

        return "$" + formatter.format(sum);
    }

    public static String formatDuration(int duration) {
        String result = "";
        int hours = duration / 60;
        int minutes = duration % 60;

        if (hours > 0) {
            result += hours + "h ";
        }
        result += minutes + "m";

        return result;
    }


    public static String printGenres(List<Genre> genres) {
        if (genres.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (Genre genre : genres) {
                result.append(genre.name).append(", ");
            }

            return result.substring(0, result.length() - 2);
        } else {
            return "";
        }
    }
}
