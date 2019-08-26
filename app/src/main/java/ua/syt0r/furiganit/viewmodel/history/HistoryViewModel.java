package ua.syt0r.furiganit.viewmodel.history;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.syt0r.furiganit.model.repository.hisotry.HistoryRepository;
import ua.syt0r.furiganit.model.repository.hisotry.firestore.FirestoreHistoryRepository;
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository;
import ua.syt0r.furiganit.model.repository.user.UserRepository;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<String>> history = new MutableLiveData<>();
    private MutableLiveData<Boolean> shouldAskSignIn = new MutableLiveData<>();

    private UserRepository userRepository;
    private HistoryRepository historyRepository;

    public void init(Context context) {

        setUserRepository(new UserRepository());

        if (userRepository.isLogged()) {
            setHistoryRepository(new FirestoreHistoryRepository());
            shouldAskSignIn.setValue(false);
        } else {
            setHistoryRepository(new LocalHistoryRepository(context));
            shouldAskSignIn.setValue(true);
        }

    }

    public void fetchHistory() {

        Disposable disposable = historyRepository.fetchHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {

                        },
                        error -> {

                        }
                );

    }

    public LiveData<List<String>> subscribeOnHistory() {
        return history;
    }

    public LiveData<Boolean> subscribeOnSignInOption() {
        return shouldAskSignIn;
    }





    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
