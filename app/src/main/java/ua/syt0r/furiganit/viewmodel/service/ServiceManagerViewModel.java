package ua.syt0r.furiganit.viewmodel.service;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import ua.syt0r.furiganit.model.repository.status.ServiceStatus;
import ua.syt0r.furiganit.model.repository.status.ServiceStatusRepository;

public class ServiceManagerViewModel extends AndroidViewModel {

    private MediatorLiveData<ServiceStatus> mutableServiceStatus = new MediatorLiveData<>();

    private ServiceStatusRepository serviceStatusRepository;

    public ServiceManagerViewModel(@NonNull Application application) {
        super(application);
        serviceStatusRepository = new ServiceStatusRepository(application);

        mutableServiceStatus.addSource(
                serviceStatusRepository.subscribeOnStatus(),
                serviceStatus -> mutableServiceStatus.setValue(serviceStatus)
        );
    }

    public LiveData<ServiceStatus> subscribeOnServiceStatus() {
        return mutableServiceStatus;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        serviceStatusRepository.unsubscribe();
    }
}
