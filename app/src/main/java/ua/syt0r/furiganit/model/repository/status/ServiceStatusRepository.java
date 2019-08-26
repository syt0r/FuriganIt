package ua.syt0r.furiganit.model.repository.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ua.syt0r.furiganit.service.FuriganaService;

public class ServiceStatusRepository {

    private static final String LAUNCHING_STATUS = "ua.syt0r.furiganit.service.launch";
    private static final String RUNNING_STATUS = "ua.syt0r.furiganit.service.run";
    private static final String STOPPED_STATUS = "ua.syt0r.furiganit.service.stop";

    private LocalBroadcastManager localBroadcastManager;
    private ServiceListener serviceListener;

    private MutableLiveData<ServiceStatus> mutableServiceStatus = new MutableLiveData<>();

    public ServiceStatusRepository(Context context) {
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        serviceListener = new ServiceListener();
    }

    public void setStatus(ServiceStatus status) {
        Intent intent = new Intent();
        switch (status) {
            case LAUNCHING:
                intent.setAction(LAUNCHING_STATUS);
                break;
            case RUNNING:
                intent.setAction(RUNNING_STATUS);
                break;
            case STOPPED:
                intent.setAction(STOPPED_STATUS);
        }
        localBroadcastManager.sendBroadcast(intent);
    }

    public LiveData<ServiceStatus> subscribeOnStatus() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LAUNCHING_STATUS);
        intentFilter.addAction(RUNNING_STATUS);
        intentFilter.addAction(STOPPED_STATUS);
        localBroadcastManager.registerReceiver(serviceListener, intentFilter);

        return mutableServiceStatus;
    }

    public void unsubscribe() {
        localBroadcastManager.unregisterReceiver(serviceListener);
    }

    private class ServiceListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null)
                switch (intent.getAction()) {
                    case LAUNCHING_STATUS:
                        mutableServiceStatus.setValue(ServiceStatus.LAUNCHING);
                        break;
                    case RUNNING_STATUS:
                        mutableServiceStatus.setValue(ServiceStatus.RUNNING);
                        break;
                    case STOPPED_STATUS:
                        mutableServiceStatus.setValue(ServiceStatus.STOPPED);
                }

        }

    }

}
