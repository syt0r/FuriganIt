package ua.syt0r.furiganit.app.serviceManager

import androidx.lifecycle.*
import ua.syt0r.furiganit.model.usecase.OverlayDrawabilityCheckUseCase
import ua.syt0r.furiganit.model.usecase.ServiceManagerStateMapperUseCase
import ua.syt0r.furiganit.model.repository.status.ServiceState
import ua.syt0r.furiganit.model.repository.status.ServiceStateRepository

class ServiceManagerViewModel(
        private val serviceStatusRepository: ServiceStateRepository,
        private val overlayDrawChecker: OverlayDrawabilityCheckUseCase,
        private val serviceStateMapper: ServiceManagerStateMapperUseCase
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

        mutableState.value = serviceStateMapper.translate(overlayDrawChecker.canDrawOverlay(), serviceState)

    }

    fun subscribeOnServiceState(): LiveData<ServiceManagerState> {
        return mutableState
    }

}
