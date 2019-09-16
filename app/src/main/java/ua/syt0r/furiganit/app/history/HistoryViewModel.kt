package ua.syt0r.furiganit.app.history

import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.remote.RemoteHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.remote.RepoState
import ua.syt0r.furiganit.model.repository.hisotry.remote.SyncAction
import ua.syt0r.furiganit.model.usecase.TextLocalizerUseCase
import ua.syt0r.furiganit.utils.SingleLiveEvent

class HistoryViewModel(
        private val localHistoryRepository: LocalHistoryRepository,
        private val remoteHistoryRepository: RemoteHistoryRepository,
        private val textLocalizerUseCase: TextLocalizerUseCase
) : ViewModel() {

    private val mutableHistory = MutableLiveData<List<HistoryItem>>()

    private val mutableRemoteRepoState = MutableLiveData<RepoState>()

    private val mutableError = SingleLiveEvent<String>()
    private val shouldAskSignIn = MutableLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    fun fetchHistory() {

        val disposable = localHistoryRepository.fetchHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mutableHistory.value = it },
                        { mutableError.value = textLocalizerUseCase.getErrorMessage(it.message) }
                )

        compositeDisposable.add(disposable)

    }

    fun syncOrRetrieveSyncAction() {

        val disposable = remoteHistoryRepository.checkRemoteRepo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mutableRemoteRepoState.value = it },
                        { mutableError.value = textLocalizerUseCase.getErrorMessage(it.message) }
                )

        compositeDisposable.add(disposable)

    }

    fun sync(syncAction: SyncAction) {

        val disposable = remoteHistoryRepository.sync(mutableHistory.value ?: listOf(), syncAction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {  },
                        { mutableError.value = textLocalizerUseCase.getErrorMessage(it.message) }
                )

        compositeDisposable.add(disposable)

    }


    fun subscribeOnHistory(): LiveData<List<HistoryItem>> {
        return mutableHistory
    }

    fun subscribeOnSignInOption(): LiveData<Boolean> {
        return shouldAskSignIn
    }

    fun subscribeOnError(): LiveData<String> {
        return mutableError
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
