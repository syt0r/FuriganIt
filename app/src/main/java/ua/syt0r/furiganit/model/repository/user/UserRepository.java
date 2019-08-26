package ua.syt0r.furiganit.model.repository.user;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Completable;

public class UserRepository {

    private FirebaseAuth firebaseAuth;

    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Completable signIn(OnActivityResultListener listener) {
        return Completable.create(emitter -> {



        });
    }

    public Completable signOut() {
        return Completable.create(emitter -> {
            firebaseAuth.signOut();
            emitter.onComplete();
        });
    }

    public boolean isLogged() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public interface OnActivityResultListener {
        void onActivityResult();
    }

}
