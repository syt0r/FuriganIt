package ua.syt0r.furiganit.model.repository.user;

import com.google.firebase.auth.FirebaseAuth;

public class UserRepository {

    private FirebaseAuth firebaseAuth;

    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signIn(OnActivityResultListener listener) {

    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public boolean isLogged() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public interface OnActivityResultListener {
        void onActivityResult();
    }

}
