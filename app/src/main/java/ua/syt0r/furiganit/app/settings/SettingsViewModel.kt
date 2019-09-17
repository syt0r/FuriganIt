package ua.syt0r.furiganit.app.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.model.usecase.TextLocalizerUseCase
import ua.syt0r.furiganit.utils.SingleLiveEvent

class SettingsViewModel(
        private val firebaseAuth: FirebaseAuth,
        private val authUI: AuthUI,
        private val textLocalizer: TextLocalizerUseCase
) : ViewModel() {

    private val isSignedIn = MutableLiveData<FirebaseUser>()
    private val mutableError = SingleLiveEvent<String>()

    init {
        firebaseAuth.addAuthStateListener {
            isSignedIn.value = it.currentUser
        }
    }

    fun signIn(activity: Activity) {

        val signInProviders = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

        val intent = authUI.createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .build()

        activity.startActivityForResult(intent, RC_SIGN_IN)

    }

    fun processActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK)
            mutableError.value = textLocalizer.getMessage(R.string.sign_in_error)

    }

    fun signOut(context: Context) {
        authUI.signOut(context)
    }

    fun isSignedIn(): LiveData<FirebaseUser> = isSignedIn
    fun subscribeOnError(): LiveData<String> = mutableError

    companion object {
        private const val RC_SIGN_IN = 198
    }

}