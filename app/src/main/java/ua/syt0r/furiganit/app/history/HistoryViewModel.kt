package ua.syt0r.furiganit.app.history

import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.remote.RemoteHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.utils.SingleLiveEvent

class HistoryViewModel(
        private val localHistoryRepository: LocalHistoryRepository,
        private val remoteHistoryRepository: RemoteHistoryRepository
) : ViewModel() {

    private val mutableHistory = MutableLiveData<List<HistoryItem>>()

    private val mutableError = SingleLiveEvent<Throwable>()
    private val shouldAskSignIn = MutableLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    fun fetchHistory() {

        val disposable = localHistoryRepository.fetchHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mutableHistory.value = it },
                        { mutableError.value = it }
                )

        compositeDisposable.add(disposable)

    }

    fun sync() {
        remoteHistoryRepository.sync(mutableHistory.value ?: listOf())
    }


    fun subscribeOnHistory(): LiveData<List<HistoryItem>> {
        return mutableHistory
    }

    fun subscribeOnSignInOption(): LiveData<Boolean> {
        return shouldAskSignIn
    }

    fun subscribeOnError(): LiveData<Throwable> {
        return mutableError
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
