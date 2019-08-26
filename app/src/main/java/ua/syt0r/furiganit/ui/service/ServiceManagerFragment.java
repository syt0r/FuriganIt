package ua.syt0r.furiganit.ui.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ua.syt0r.furiganit.model.repository.status.ServiceStatus;
import ua.syt0r.furiganit.service.FuriganaService;
import ua.syt0r.furiganit.R;
import ua.syt0r.furiganit.viewmodel.service.ServiceManagerViewModel;

public class ServiceManagerFragment extends Fragment {

    private ServiceManagerViewModel viewModel;

    private TextView textView;
    private Button button;

    private boolean canDrawOverlays;
    //prevent from attempt to press button while loading
    private boolean isLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ServiceManagerViewModel.class);
        isLoading = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_service_manager, container, false);
        button = root.findViewById(R.id.button);
        textView = root.findViewById(R.id.text);

        button.setOnClickListener( view -> {

            if (isLoading){
                Toast.makeText(getContext(), R.string.wait,Toast.LENGTH_SHORT).show();
                return;
            }

            if (!canDrawOverlays && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){

                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getContext().getPackageName())
                );
                startActivityForResult(intent, 0);
                return;
            }

            button.setText(R.string.loading);

            isLoading = true;

            Intent intent = new Intent(getContext(), FuriganaService.class);

            if (viewModel.subscribeOnServiceStatus().getValue() == ServiceStatus.STOPPED)
                root.getContext().startService(intent);
            else
                root.getContext().stopService(intent);

        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateText();
    }

    private void checkPermission(){

        canDrawOverlays = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            canDrawOverlays = Settings.canDrawOverlays(getContext());

        updateText();

    }

    private void updateText(){

        if (!canDrawOverlays){

            button.setText(R.string.go_to_settings);
            textView.setText(R.string.got_to_sett_hint);

        } else {

            ServiceStatus serviceStatus = viewModel.subscribeOnServiceStatus().getValue();
            if (serviceStatus != null)
                switch (serviceStatus) {
                    case STOPPED:
                        button.setText(R.string.start);
                        textView.setText(R.string.start_hint);
                        break;
                    case LAUNCHING:
                    case RUNNING:
                        button.setText(R.string.stop);
                        textView.setText(R.string.stop_hint);
                }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPermission();
    }

}
