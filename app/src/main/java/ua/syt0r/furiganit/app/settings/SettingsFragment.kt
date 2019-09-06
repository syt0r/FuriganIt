package ua.syt0r.furiganit.app.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ua.syt0r.furiganit.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
