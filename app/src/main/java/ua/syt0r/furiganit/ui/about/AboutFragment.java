package ua.syt0r.furiganit.ui.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import ua.syt0r.furiganit.R;

public class AboutFragment extends Fragment implements BillingProcessor.IBillingHandler {

    private static final String PRODUCT_ID = "ua.syt0r.furiganit.support_item";

    private ViewGroup purchaseLayout;

    private BillingProcessor billingProcessor;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (BillingProcessor.isIabServiceAvailable(context)) {
            billingProcessor = new BillingProcessor(context, getString(R.string.license_key),this);
            billingProcessor.initialize();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        purchaseLayout = root.findViewById(R.id.purchase_layout);
        purchaseLayout.setVisibility(View.GONE);

        root.findViewById(R.id.source_code_button).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/SYtor/FuriganIt"));
            startActivity(browserIntent);
        });

        root.findViewById(R.id.support_button)
                .setOnClickListener(view -> billingProcessor.purchase(getActivity(), PRODUCT_ID));

        return root;
    }

    //Billing stuff

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (billingProcessor != null)
            billingProcessor.release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (billingProcessor != null && !billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(getContext(), R.string.thanks, Toast.LENGTH_LONG).show();
        billingProcessor.consumePurchase(PRODUCT_ID);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(getContext(), R.string.bill_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {
        purchaseLayout.setVisibility(View.VISIBLE);
    }

}
