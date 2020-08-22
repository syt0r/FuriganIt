package ua.syt0r.furiganit.app.main.screens.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.R

class SettingsFragment : PreferenceFragmentCompat() {

	private val settingsViewModel: SettingsViewModel by viewModel()

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.preferences, rootKey)
	}
}
