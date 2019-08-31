package ua.syt0r.furiganit.viewmodel.history;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.syt0r.furiganit.model.entity.HistoryItem;
import ua.syt0r.furiganit.model.repository.hisotry.HistoryRepository;
import ua.syt0r.furiganit.model.repository.hisotry.firestore.FirestoreHistoryRepository;
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository;
import ua.syt0r.furiganit.model.repository.user.UserRepository;

public class HistoryViewModel extends AndroidViewModel {

    private MutableLiveData<List<HistoryItem>> mutableHistory = new MutableLiveData<>();

    private MutableLiveData<Throwable> mutableError = new MutableLiveData<>();
    private MutableLiveData<Boolean> shouldAskSignIn = new MutableLiveData<>();

    private UserRepository userRepository;
    private HistoryRepository localHistoryRepository;
    private HistoryRepository remoteHistoryRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HistoryViewModel(Application application) {
        super(application);

        userRepository = new UserRepository();
        localHistoryRepository = new LocalHistoryRepository(application);

        if (userRepository.isLogged())
            remoteHistoryRepository = new FirestoreHistoryRepository();

    }

    public void removeItems(int position) {

        List<HistoryItem> history = mutableHistory.getValue();
        if (history != null) {

            HistoryItem item = history.get(position);

            Disposable disposable = localHistoryRepository.remove(item)
                    .andThen(remoteHistoryRepository != null ?
                            remoteHistoryRepository.remove(item) : Completable.complete())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {

                            },
                            error -> {

                            });

            compositeDisposable.add(disposable);
        }

    }

    public void loadHistory() {

    }

    public void syncHistory() {

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

}
