package com.bogdanorzea.popularmovies.utility

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.bogdanorzea.popularmovies.R
import java.text.DecimalFormat

fun Context.getPreferredSortRule(): String {
    return PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString(this.getString(R.string.pref_sort_by), this.getString(R.string.pref_sort_by_popularity))
}

fun Long.toMoneyString(): String {
    return if (this > 0) {
        "\$${DecimalFormat("###,###,###.##").format(this)}"
    } else {
        "Unknown"
    }
}

fun Int.toTimeString(): String {
    return if (this > 0) {
        "${if (this >= 60) "${this / 60}h " else ""}${this % 60}m"
    } else {
        "Unknown"
    }
}

fun String.addQuotes() = if (this.isNotEmpty()) "\"$this\"" else ""

fun String.addParenthesis() = if (this.isNotEmpty()) "($this)" else ""
