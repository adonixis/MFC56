package ru.adonixis.mfc56.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.adonixis.mfc56.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}