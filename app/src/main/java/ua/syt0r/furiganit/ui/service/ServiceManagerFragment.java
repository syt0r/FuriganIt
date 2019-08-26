package ua.syt0r.furiganit.ui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ua.syt0r.furiganit.FuriganaService;
import ua.syt0r.furiganit.R;

public class ServiceManagerFragment extends Fragment {

    private TextView textView;
    private Button button;

    private boolean canDrawOverlays;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    //prevent from attempt to press button while loading
    private boolean isLoading;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        broadcastReceiver = new ServiceListener();

        isLoading = false;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
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

            if (!FuriganaService.isRunning)
                getContext().startService(intent);
            else
                getContext().stopService(intent);

        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FuriganaService.START_ACTION);
        intentFilter.addAction(FuriganaService.STOP_ACTION);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        updateText();
    }

    @Override
    public void onPause() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onPause();
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

        }else if (FuriganaService.isRunning){

            button.setText(R.string.stop);
            textView.setText(R.string.stop_hint);

        }else{

            button.setText(R.string.start);
            textView.setText(R.string.start_hint);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPermission();
    }

    private class ServiceListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case FuriganaService.START_ACTION:
                case FuriganaService.STOP_ACTION:
                    isLoading = false;
                    updateText();
                    break;
            }

        }

    }

}
