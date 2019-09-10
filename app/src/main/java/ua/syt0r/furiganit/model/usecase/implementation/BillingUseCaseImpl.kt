package ua.syt0r.furiganit.model.usecase.implementation

import android.content.Context
import com.android.billingclient.api.*
import io.reactivex.*
import io.reactivex.subjects.BehaviorSubject
import ua.syt0r.furiganit.model.usecase.BillingUseCase
import java.lang.Exception

class BillingUseCaseImpl(context: Context) : BillingUseCase,
        BillingClientStateListener,
        PurchasesUpdatedListener {

    //TODO inject activity, return errors to user

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

        loadSkuDetails().flatMapCompletable { skuDetails ->
            Completable.fromRunnable {
                val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .build()
                //val responseCode = billingClient.launchBillingFlow(activity, flowParams)
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
        purchases?.getOrNull(0)?.also { purchase ->
            consumePurchase(purchase.purchaseToken)
        }
    }

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

        billingClient.consumeAsync(consumeParams) { billingResult, purchaseToken ->
            /*when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> emitter.onComplete()
                else -> emitter.onError(Throwable())
            }*/
        }

    }


    companion object {
        private const val PRODUCT_ID = "ua.syt0r.furiganit.support_item"
    }

}