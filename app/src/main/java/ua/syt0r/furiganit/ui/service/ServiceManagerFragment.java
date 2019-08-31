package ua.syt0r.furiganit.ui.service;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import ua.syt0r.furiganit.model.repository.status.ServiceStatus;
import ua.syt0r.furiganit.service.FuriganaService;
import ua.syt0r.furiganit.R;
import ua.syt0r.furiganit.viewmodel.service.ServiceManagerViewModel;

/*

 */
public class ServiceManagerFragment extends Fragment {

    private ServiceManagerViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ServiceManagerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_service_manager, container, false);

        Button button = root.findViewById(R.id.button);
        TextView textView = root.findViewById(R.id.text);

        viewModel.subscribeOnServiceStatus().observe(this, status -> {
            switch (status) {

                case CANT_DRAW_OVERLAY:

                    button.setText(R.string.go_to_settings);
                    textView.setText(R.string.got_to_sett_hint);

                    button.setOnClickListener(view -> requestDrawOverlayPermission());

                    break;

                case STOPPED:

                    button.setText(R.string.start);
                    textView.setText(R.string.start_hint);

                    button.setOnClickListener(view -> {
                        Intent intent = new Intent(getContext(), FuriganaService.class);
                        root.getContext().startService(intent);
                    });

                    break;

                case LAUNCHING:

                    button.setText(R.string.starting);
                    textView.setText(R.string.starting_hint);

                    button.setOnClickListener(view -> Toast.makeText(getContext(),
                            "Wait a moment, please", Toast.LENGTH_SHORT).show());

                    break;

                case RUNNING:

                    button.setText(R.string.stop);
                    textView.setText(R.string.stop_hint);

                    button.setOnClickListener(view -> {
                        Intent intent = new Intent(getContext(), FuriganaService.class);
                        root.getContext().stopService(intent);
                    });

                    break;
            }
        });

        return root;
    }

    private void requestDrawOverlayPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent( Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getContext().getPackageName()));
            startActivityForResult(intent, 0);
        }
    }

}
