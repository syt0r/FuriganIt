package ua.syt0r.furiganit.app.main.screens.service_manager

import androidx.lifecycle.*
import ua.syt0r.furiganit.core.overlay.OverlayManager
import ua.syt0r.furiganit.core.service.ServiceState
import ua.syt0r.furiganit.core.service.ServiceStateObservable

class ServiceManagerViewModel(
        private val serviceStatusObservable: ServiceStateObservable,
        private val overlayManager: OverlayManager
) : ViewModel(), LifecycleObserver {

    private val mutableState = MediatorLiveData<ServiceManagerState>()

    init {
        mutableState.addSource(serviceStatusObservable.subscribeToStateChanges()) { updateState() }
        updateState()
    }

    override fun onCleared() {
        super.onCleared()
        serviceStatusObservable.unsubscribe()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun updateState() {
        val serviceState = serviceStatusObservable.subscribeToStateChanges().value
                ?: ServiceState.STOPPED

        val canDrawOverlay = overlayManager.canDrawOverlay()
        mutableState.value = serviceState.toServiceManagerState(canDrawOverlay)
    }

    fun subscribeOnServiceState(): LiveData<ServiceManagerState> {
        return mutableState
    }

}
