package ua.syt0r.furiganit.app.serviceManager

import androidx.lifecycle.*
import ua.syt0r.furiganit.model.usecase.OverlayDrawabilityChecker
import ua.syt0r.furiganit.model.usecase.ServiceManagerStateMapper
import ua.syt0r.furiganit.model.repository.serviceState.ServiceState
import ua.syt0r.furiganit.model.repository.serviceState.ServiceStateRepository

class ServiceManagerViewModel(
    private val serviceStatusRepository: ServiceStateRepository,
    private val overlayDrawChecker: OverlayDrawabilityChecker,
    private val serviceStateMapper: ServiceManagerStateMapper
) : ViewModel(), LifecycleObserver {

    private val mutableState = MediatorLiveData<ServiceManagerState>()

    init {
        mutableState.addSource(serviceStatusRepository.subscribeOnStatus()) { updateState() }
        updateState()
    }

    override fun onCleared() {
        super.onCleared()
        serviceStatusRepository.unsubscribe()
    }

    //Update view after overlay settings had changed
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun updateState() {

        val serviceState = serviceStatusRepository.subscribeOnStatus().value
                ?: ServiceState.STOPPED

        mutableState.value = serviceStateMapper.map(overlayDrawChecker.canDrawOverlay(), serviceState)

    }

    fun subscribeOnServiceState(): LiveData<ServiceManagerState> {
        return mutableState
    }

}
