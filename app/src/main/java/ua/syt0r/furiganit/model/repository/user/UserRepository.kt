package ua.syt0r.furiganit.model.repository.user

import com.google.firebase.auth.FirebaseAuth

class UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    val isLogged: Boolean
        get() = firebaseAuth.currentUser != null

    fun signIn(listener: OnActivityResultListener) {

    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    interface OnActivityResultListener {
        fun onActivityResult()
    }

}
