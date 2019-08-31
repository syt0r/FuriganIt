package ua.syt0r.furiganit.viewmodel.service;

import android.app.Application;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import ua.syt0r.furiganit.model.repository.status.ServiceStatus;
import ua.syt0r.furiganit.model.repository.status.ServiceStatusRepository;

public class ServiceManagerViewModel extends AndroidViewModel {

    public enum ServiceManagerStates { CANT_DRAW_OVERLAY, STOPPED, LAUNCHING, RUNNING }

    private ServiceStatusRepository serviceStatusRepository;

    private MediatorLiveData<ServiceManagerStates> mutableState = new MediatorLiveData<>();

    public ServiceManagerViewModel(@NonNull Application application) {
        super(application);
        serviceStatusRepository = new ServiceStatusRepository(application);
        mutableState.addSource(serviceStatusRepository.subscribeOnStatus(), status -> {
            updateState();
        });
        updateState();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        serviceStatusRepository.unsubscribe();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() { updateState(); }

    private boolean canDrawOverlay() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            return Settings.canDrawOverlays(getApplication());
        return true;
    }

    private void updateState() {

        ServiceStatus status = serviceStatusRepository.subscribeOnStatus().getValue();
        if (status == null) status = ServiceStatus.STOPPED;

        if (canDrawOverlay())
            switch (status) {
                case STOPPED:
                    mutableState.setValue(ServiceManagerStates.STOPPED);
                    break;
                case LAUNCHING:
                    mutableState.setValue(ServiceManagerStates.LAUNCHING);
                    break;
                case RUNNING:
                    mutableState.setValue(ServiceManagerStates.RUNNING);
            }
        else
            mutableState.setValue(ServiceManagerStates.CANT_DRAW_OVERLAY);

    }


    public LiveData<ServiceManagerStates> subscribeOnServiceStatus() {
        return mutableState;
    }

}
