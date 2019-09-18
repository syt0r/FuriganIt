package ua.syt0r.furiganit.app.history

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.remote.RemoteHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.remote.RepoState
import ua.syt0r.furiganit.model.repository.hisotry.remote.SyncAction
import ua.syt0r.furiganit.model.repository.hisotry.remote.SyncState
import ua.syt0r.furiganit.model.usecase.TextLocalizerUseCase
import ua.syt0r.furiganit.utils.SingleLiveEvent
import java.lang.IllegalStateException

class HistoryViewModel(
        private val localHistoryRepository: LocalHistoryRepository,
        private val remoteHistoryRepository: RemoteHistoryRepository,
        private val firebaseAuth: FirebaseAuth,
        private val textLocalizerUseCase: TextLocalizerUseCase
) : ViewModel() {

    private val mutableHistory = MutableLiveData<List<HistoryItem>>()
    private val mutableSyncState = MutableLiveData<SyncState>()
    private val mutableMessage = SingleLiveEvent<String>()
    private val mutableEvent = SingleLiveEvent<Event>()

    private val compositeDisposable = CompositeDisposable()

    fun fetchHistory() {

        val disposable = localHistoryRepository
                .fetchHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mutableHistory.value = it },
                        { mutableMessage.value = textLocalizerUseCase.getErrorMessage(it.message) }
                )

        compositeDisposable.add(disposable)

    }

    fun checkRemoteRepoState() {

        if (firebaseAuth.currentUser == null) {
            mutableEvent.value = Event.GO_TO_SIGNUP_SCREEN
            return
        }

        val disposable = remoteHistoryRepository
                .checkRemoteRepo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { repoState ->
                            when(repoState) {
                                RepoState.NEWER -> { sync(SyncAction.OVERWRITE_LOCAL) }
                                RepoState.OUTDATED -> { sync(SyncAction.OVERWRITE_REMOTE) }
                                RepoState.DIFFERENT -> {
                                    mutableEvent.value = Event.SELECT_SYNC_ACTION
                                }
                                null -> throw IllegalStateException("Unknown repoState")
                            }
                        },
                        { mutableMessage.value = textLocalizerUseCase.getErrorMessage(it.message) }
                )

        compositeDisposable.add(disposable)

    }

    fun sync(syncAction: SyncAction) {

        val disposable = remoteHistoryRepository
                .sync(mutableHistory.value ?: listOf(), syncAction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {

                    mutableSyncState.value = when(syncAction) {
                        SyncAction.OVERWRITE_LOCAL -> SyncState.DOWNLOADING_CHANGES
                        SyncAction.OVERWRITE_REMOTE -> SyncState.UPLOADING_CHANGES
                        SyncAction.MERGE -> SyncState.MERGING
                    }

                }
                .subscribe(
                        {  },
                        { mutableMessage.value = textLocalizerUseCase.getErrorMessage(it.message) }
                )

        compositeDisposable.add(disposable)

    }


    fun subscribeOnHistory(): LiveData<List<HistoryItem>> {
        return mutableHistory
    }

    fun subscribeOnMessage(): LiveData<String> {
        return mutableMessage
    }

    fun subscribeOnEvents(): LiveData<Event> {
        return mutableEvent
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    enum class Event { SELECT_SYNC_ACTION, GO_TO_SIGNUP_SCREEN }

}
