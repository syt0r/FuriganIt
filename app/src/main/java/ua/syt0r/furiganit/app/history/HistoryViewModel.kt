package ua.syt0r.furiganit.app.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.HistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.firestore.FirestoreHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.model.repository.user.UserRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableHistory = MutableLiveData<List<HistoryItem>>()

    private val mutableError = MutableLiveData<Throwable>()
    private val shouldAskSignIn = MutableLiveData<Boolean>()

    private val userRepository: UserRepository = UserRepository()
    private val localHistoryRepository: HistoryRepository
    private var remoteHistoryRepository: HistoryRepository? = null

    private val compositeDisposable = CompositeDisposable()

    init {

        localHistoryRepository = LocalHistoryRepository(application)

        if (userRepository.isLogged)
            remoteHistoryRepository = FirestoreHistoryRepository()

    }

    fun removeItems(position: Int) {

        val history = mutableHistory.value
        if (history != null) {

            val item = history[position]

            val disposable = localHistoryRepository.remove(item)
                    .andThen(if (remoteHistoryRepository != null)
                        remoteHistoryRepository!!.remove(item)
                    else
                        Completable.complete())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {

                            },
                            { error ->

                            })

            compositeDisposable.add(disposable)
        }

    }

    fun loadHistory() {

    }

    fun syncHistory() {

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

}
