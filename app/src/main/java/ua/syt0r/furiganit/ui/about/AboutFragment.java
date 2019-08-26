package ua.syt0r.furiganit.ui.about;

import android.content.Context;
import android.content.Intent;
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

public class AboutFragment extends Fragment {

    private static final String PRODUCT_ID = "ua.syt0r.furiganit.support_item";

    private BillingProcessor billingProcessor;
    private boolean isBillingAvailable;
    private boolean isBillingReady;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        isBillingAvailable = BillingProcessor.isIabServiceAvailable(context);
        isBillingReady = false;
        if (isBillingAvailable){
            billingProcessor = new BillingProcessor(context, getString(R.string.license_key),
                    new BillingHandler());
            //billingProcessor.initialize();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        Button button = root.findViewById(R.id.button);

        button.setOnClickListener(view -> {

            //TODO toggle button visibility if billing not available

            if (isBillingReady)
                billingProcessor.purchase(getActivity(), PRODUCT_ID);
            else
                Toast.makeText(getContext(), R.string.bill_not_ready, Toast.LENGTH_SHORT).show();

        });

        return root;
    }

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

    private class BillingHandler implements BillingProcessor.IBillingHandler{

        @Override
        public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
            Toast.makeText(getContext(), R.string.thanks, Toast.LENGTH_LONG).show();
            billingProcessor.consumePurchase(PRODUCT_ID);
        }

        @Override
        public void onPurchaseHistoryRestored() {

        }

        @Override
        public void onBillingError(int errorCode, @Nullable Throwable error) {
            Toast.makeText(getContext(), R.string.bill_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBillingInitialized() {
            isBillingReady = true;
        }
    }

}
