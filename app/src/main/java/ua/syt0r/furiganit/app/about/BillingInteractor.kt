package ua.syt0r.furiganit.app.about

import android.content.Context
import com.android.billingclient.api.*
import io.reactivex.*
import io.reactivex.subjects.BehaviorSubject

class BillingInteractor(context: Context) : BillingUseCase,
        BillingClientStateListener,
        PurchasesUpdatedListener {

    private var isConnecting = false

    private val billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

    private val availabilityBehaviourSubject = BehaviorSubject.create<Boolean>()

    override fun connect() {
        if (!isConnecting) {
            isConnecting = true
            billingClient.startConnection(this)
        }
    }

    override fun disconnect() = billingClient.endConnection()


    override fun isAvailable() = availabilityBehaviourSubject

    override fun purchase() = Completable.create { emitter ->

        val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(PRODUCT_ID)
                .build()

        billingClient.consumeAsync(consumeParams) { billingResult, purchaseToken ->
            when(billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> emitter.onComplete()
                else -> emitter.onError(Throwable())
            }
        }

    }

    override fun onBillingSetupFinished(billingResult: BillingResult?) {
        isConnecting = false
        availabilityBehaviourSubject.onNext(
                billingResult?.responseCode == BillingClient.BillingResponseCode.OK
        )
    }

    override fun onBillingServiceDisconnected() {

    }

    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {

    }


    companion object {
        private const val PRODUCT_ID = "ua.syt0r.furiganit.support_item"
    }

}