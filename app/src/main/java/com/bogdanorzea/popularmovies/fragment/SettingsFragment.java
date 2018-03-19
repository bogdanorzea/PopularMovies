package com.bogdanorzea.popularmovies.fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.data.MoviesContract;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int preferenceCount = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            setSummary(preference);
        }


        Preference button = findPreference(getString(R.string.pref_clear_favorites_key));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            ContentValues values = new ContentValues();
                            values.put(MoviesContract.MovieEntry.COLUMN_NAME_FAVORITE, 0);

                            getContext().getContentResolver().update(MoviesContract.CONTENT_URI, values, null, null);
                        }

                        dialog.dismiss();
                    }
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.confirm_clear_favorites)
                        .setPositiveButton(R.string.button_yes, dialogClickListener)
                        .setNegativeButton(R.string.button_no, dialogClickListener)
                        .show();

                return true;
            }
        });
    }

    private void setSummary(Preference preference) {
        if (preference instanceof PreferenceGroup) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
            for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
                setSummary(preferenceGroup.getPreference(i));
            }
        }

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            preference.setSummary(listPreference.getEntry());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String preferenceKey) {
        Preference preference = findPreference(preferenceKey);
        if (null != preference) {
            setSummary(preference);
        }
    }
}
