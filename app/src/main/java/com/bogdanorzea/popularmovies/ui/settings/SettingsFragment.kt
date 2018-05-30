package com.bogdanorzea.popularmovies.ui.settings

import android.content.ContentValues
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceGroup

import com.bogdanorzea.popularmovies.R
import com.bogdanorzea.popularmovies.data.MoviesContract

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        for (i in 0 until preferenceScreen.preferenceCount) {
            setSummary(preferenceScreen.getPreference(i))
        }

        findPreference(getString(R.string.pref_clear_favorites_key))?.setOnPreferenceClickListener {
            val dialogClickListener = { dialog: DialogInterface, which: Int ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val values = ContentValues()
                        values.put(MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE, 0)

                        context?.contentResolver?.update(MoviesContract.CONTENT_URI, values, null, null)
                    }
                }

                dialog.dismiss()
            }

            context?.let {
                AlertDialog.Builder(it)
                        .setMessage(R.string.confirm_clear_favorites)
                        .setPositiveButton(R.string.button_yes, dialogClickListener)
                        .setNegativeButton(R.string.button_no, dialogClickListener)
                        .show()
            }

            true
        }
    }

    private fun setSummary(preference: Preference) {
        when (preference) {
            is PreferenceGroup -> {
                for (i in 0 until preference.preferenceCount)
                    setSummary(preference.getPreference(i))
            }
            is ListPreference -> preference.setSummary(preference.entry)
        }
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, preferenceKey: String?) {
        findPreference(preferenceKey)?.let {
            setSummary(it)
        }
    }
}
