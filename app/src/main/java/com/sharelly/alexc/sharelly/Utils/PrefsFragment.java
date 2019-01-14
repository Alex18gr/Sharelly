package com.sharelly.alexc.sharelly.Utils;

import android.os.Bundle;

import com.sharelly.alexc.sharelly.R;

import androidx.preference.PreferenceFragmentCompat;

public class PrefsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
