package ua.syt0r.furiganit.app.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.furiganit.model.usecase.BillingUseCase
import ua.syt0r.furiganit.utils.SingleLiveEvent

class AboutViewModel(private val billingUseCase: BillingUseCase) : ViewModel() {

    private val mutableBillingAvailability = MutableLiveData<Boolean>()
    private val mutableMessage = SingleLiveEvent<String>()

    private val compositeDisposable = CompositeDisposable()

    fun checkBillingAvailability() {

        billingUseCase.connect()

        val disposable = billingUseCase.isAvailable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mutableBillingAvailability.value = it },
                        { error -> mutableMessage.value = error.message ?: "Error" }
                )

        compositeDisposable.add(disposable)

    }

    fun purchase() {

        val disposable = billingUseCase.purchase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mutableMessage.value = "Thank you for support!" },
                        { error -> mutableMessage.value = error.message ?: "Error" }
                )

        compositeDisposable.add(disposable)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        billingUseCase.disconnect()
    }

    fun subscribeOnBillingAvailability(): LiveData<Boolean> = mutableBillingAvailability
    fun subscribeOnMessage(): LiveData<String> = mutableMessage

}