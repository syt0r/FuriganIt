package ua.syt0r.furiganit.app.about

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.lang.Exception

class BillingManager(context: Context) : BillingClientStateListener, PurchasesUpdatedListener {

    //TODO subscribe on purchase through onPurchaseUpdated method

    private val billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

    private val availabilityBehaviourSubject = BehaviorSubject.create<Boolean>()


    // Public interface

    fun purchase(activity: Activity) = Completable.create { emitter ->

        loadSkuDetails().flatMapCompletable { skuDetails ->
            Completable.fromRunnable {
                val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .build()
                val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            }
        }.subscribe(emitter::onComplete, emitter::onError)

    }

    fun checkBillingAvailability(): Observable<Boolean> = availabilityBehaviourSubject

    fun connect() = billingClient.startConnection(this)

    fun disconnect() = billingClient.endConnection()


    // Billing library callbacks

    override fun onBillingSetupFinished(billingResult: BillingResult?) {
        availabilityBehaviourSubject
                .onNext(billingResult?.responseCode == BillingClient.BillingResponseCode.OK)
    }

    override fun onBillingServiceDisconnected() {

    }

    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        purchases?.getOrNull(0)?.also { purchase ->
            consumePurchase(purchase.purchaseToken)
        }
    }

    // Custom helper functions

    private fun loadSkuDetails() = Single.create<SkuDetails> { emitter ->

        val skuList = listOf(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->

            val skuDetails = skuDetailsList?.getOrNull(0)
            if (skuDetails != null)
                emitter.onSuccess(skuDetails)
            else
                emitter.onError(Exception("Cant get purchase details"))
        }

    }

    private fun consumePurchase(purchaseToken: String) {

        val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()

        billingClient.consumeAsync(consumeParams) { billingResult, purchaseToken -> }

    }

    companion object {
        private const val PRODUCT_ID = "ua.syt0r.furiganit.support_item"
    }

}