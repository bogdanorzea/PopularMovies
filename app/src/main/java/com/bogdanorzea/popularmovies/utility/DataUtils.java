package com.bogdanorzea.popularmovies.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.bogdanorzea.popularmovies.R;

import java.text.DecimalFormat;

public class DataUtils {
    public static String getPreferredSortRule(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        return sharedPreferences.getString(
                context.getString(R.string.pref_sort_by),
                context.getString(R.string.pref_sort_by_popularity));
    }

    public static String formatMoney(long sum) {
        if (sum > 0) {
            DecimalFormat formatter = new DecimalFormat("###,###,###.##");

            return "$" + formatter.format(sum);
        } else {
            return "Unknown";
        }
    }

    public static String formatDuration(int duration) {
        if (duration > 0) {
            String result = "";
            int hours = duration / 60;
            int minutes = duration % 60;

            if (hours > 0) {
                result += hours + "h ";
            }
            result += minutes + "m";

            return result;
        } else {
            return "Unknown";
        }
    }


    public static String quoteString(String string) {
        if (string != null && string.length() > 0) {
            return String.format("\"%s\"", string);
        }

        return "";
    }

    public static String addParenthesis(String string) {
        if (string != null && string.length() > 0) {
            return String.format("(%s)", string);
        }

        return "";
    }


}
