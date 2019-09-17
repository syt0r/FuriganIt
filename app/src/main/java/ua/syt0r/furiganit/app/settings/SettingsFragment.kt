package ua.syt0r.furiganit.app.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.R

class SettingsFragment : PreferenceFragmentCompat() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val signInPreference: Preference = findPreference(getString(R.string.preference_key_sign_in))!!
        val signOutPreference: Preference = findPreference(getString(R.string.preference_key_sign_out))!!

        signInPreference.setOnPreferenceClickListener {
            settingsViewModel.signIn(requireActivity())
            true
        }

        signOutPreference.setOnPreferenceClickListener {
            settingsViewModel.signOut(requireContext())
            true
        }

        settingsViewModel.isSignedIn().observe(this, Observer { user ->

            if (user != null) {
                signInPreference.title = user.displayName
                signInPreference.isSelectable = false
                signOutPreference.isVisible = true
            } else {
                signInPreference.title = getString(R.string.sign_in)
                signInPreference.isSelectable = true
                signOutPreference.isVisible = false
            }

        })

        settingsViewModel.subscribeOnError().observe(this, Observer {
            Snackbar.make(view!!, it, Snackbar.LENGTH_SHORT).show()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        settingsViewModel.processActivityResult(requestCode, resultCode, data)
    }


}
