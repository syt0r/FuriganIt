package ua.syt0r.furiganit.app.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import ua.syt0r.furiganit.R

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var signInPreference: Preference
    private lateinit var signOutPreference: Preference

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FirebaseApp.initializeApp(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        signInPreference = findPreference(getString(R.string.sign_in))!!
        signOutPreference = findPreference(getString(R.string.sign_out))!!

        signInPreference.setOnPreferenceClickListener {

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    listOf(AuthUI.IdpConfig.GoogleBuilder().build())
                            )
                            .build(),
                    RC_SIGN_IN
            )

            true
        }

        signOutPreference.setOnPreferenceClickListener {

            AuthUI.getInstance().signOut(requireContext())
            updateAccountInfo()

            true
        }

        updateAccountInfo()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                updateAccountInfo()
            } else {
                Snackbar.make(view!!, "Error", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    private fun updateAccountInfo() {

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            signInPreference.title = user.displayName
            signOutPreference.isVisible = true
        } else {
            signOutPreference.isVisible = false
        }


    }

    companion object {
        private const val RC_SIGN_IN = 198
    }

}
