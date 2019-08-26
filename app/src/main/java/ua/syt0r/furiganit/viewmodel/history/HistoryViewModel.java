package ua.syt0r.furiganit.viewmodel.history;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.syt0r.furiganit.model.entity.HistoryItem;
import ua.syt0r.furiganit.model.repository.hisotry.HistoryRepository;
import ua.syt0r.furiganit.model.repository.hisotry.firestore.FirestoreHistoryRepository;
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository;
import ua.syt0r.furiganit.model.repository.user.UserRepository;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<HistoryItem>> mutableHistory = new MutableLiveData<>();
    private MutableLiveData<Throwable> mutableError = new MutableLiveData<>();
    private MutableLiveData<Boolean> shouldAskSignIn = new MutableLiveData<>();

    private UserRepository userRepository;
    private HistoryRepository historyRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        mutableHistory.setValue(null);
        mutableError.setValue(null);

        Disposable disposable = historyRepository.fetchHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> mutableHistory.setValue(data),
                        error -> mutableError.setValue(error)
                );

        compositeDisposable.add(disposable);

    }

    public void removeItemAtPos(int position) {

    }

    public LiveData<List<HistoryItem>> subscribeOnHistory() {
        return mutableHistory;
    }

    public LiveData<Boolean> subscribeOnSignInOption() {
        return shouldAskSignIn;
    }

    public LiveData<Throwable> subscribeOnError() {
        return mutableError;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
