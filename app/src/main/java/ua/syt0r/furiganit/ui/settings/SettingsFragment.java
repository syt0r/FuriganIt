package ua.syt0r.furiganit.ui.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import ua.syt0r.furiganit.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
