package ua.syt0r.furiganit.app.about

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.model.usecase.TextLocalizer
import ua.syt0r.furiganit.utils.SingleLiveEvent

class AboutViewModel(
        private val billingManager: BillingManager,
        private val textLocalizer: TextLocalizer
) : ViewModel() {

    private val mutableBillingAvailability = MutableLiveData<Boolean>()
    private val mutableMessage = SingleLiveEvent<String>()

    private val compositeDisposable = CompositeDisposable()

    init { billingManager.connect() }

    fun checkBillingAvailability() {

        val disposable = billingManager.checkBillingAvailability()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mutableBillingAvailability.value = it }

        compositeDisposable.add(disposable)

    }

    fun purchase(activity: Activity) {

        val disposable = billingManager.purchase(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mutableMessage.value = textLocalizer.getMessage(R.string.thanks) },
                        { mutableMessage.value = textLocalizer.getErrorMessage(it.message) }
                )

        compositeDisposable.add(disposable)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        billingManager.disconnect()
    }

    fun subscribeOnBillingAvailability(): LiveData<Boolean> = mutableBillingAvailability
    fun subscribeOnMessage(): LiveData<String> = mutableMessage

}