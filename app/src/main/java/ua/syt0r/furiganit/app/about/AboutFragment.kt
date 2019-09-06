package ua.syt0r.furiganit.app.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import ua.syt0r.furiganit.R

class AboutFragment : Fragment(), BillingProcessor.IBillingHandler {

    private var purchaseLayout: ViewGroup? = null

    private var billingProcessor: BillingProcessor? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (BillingProcessor.isIabServiceAvailable(context)) {
            billingProcessor = BillingProcessor(context, getString(R.string.license_key), this)
            billingProcessor!!.initialize()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)

        purchaseLayout = root.findViewById(R.id.purchase_layout)
        purchaseLayout!!.visibility = View.GONE

        root.findViewById<View>(R.id.source_code_button).setOnClickListener { view ->
            val browserIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/SYtor/FuriganIt"))
            startActivity(browserIntent)
        }

        root.findViewById<View>(R.id.support_button)
                .setOnClickListener { view -> billingProcessor!!.purchase(activity, PRODUCT_ID) }

        return root
    }

    //Billing stuff

    override fun onDestroy() {
        super.onDestroy()
        if (billingProcessor != null)
            billingProcessor!!.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (billingProcessor != null && !billingProcessor!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        Toast.makeText(context, R.string.thanks, Toast.LENGTH_LONG).show()
        billingProcessor!!.consumePurchase(PRODUCT_ID)
    }

    override fun onPurchaseHistoryRestored() {

    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Toast.makeText(context, R.string.bill_error, Toast.LENGTH_SHORT).show()
    }

    override fun onBillingInitialized() {
        purchaseLayout!!.visibility = View.VISIBLE
    }

    companion object {
        private const val PRODUCT_ID = "ua.syt0r.furiganit.support_item"
    }

}
