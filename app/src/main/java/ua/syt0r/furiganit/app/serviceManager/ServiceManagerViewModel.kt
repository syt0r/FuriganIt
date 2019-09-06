package ua.syt0r.furiganit.app.serviceManager

import android.app.Application
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.OnLifecycleEvent

import ua.syt0r.furiganit.model.repository.status.ServiceStatus
import ua.syt0r.furiganit.model.repository.status.ServiceStatusRepository

class ServiceManagerViewModel(application: Application) : AndroidViewModel(application) {

    private val serviceStatusRepository = ServiceStatusRepository(application)

    private val mutableState = MediatorLiveData<ServiceManagerStates>()

    enum class ServiceManagerStates {
        CANT_DRAW_OVERLAY, STOPPED, LAUNCHING, RUNNING
    }

    init {
        mutableState.addSource(serviceStatusRepository.subscribeOnStatus()) { status -> updateState() }
        updateState()
    }

    override fun onCleared() {
        super.onCleared()
        serviceStatusRepository.unsubscribe()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun onResume() {
        updateState()
    }

    private fun canDrawOverlay(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            Settings.canDrawOverlays(getApplication())
        else true
    }

    private fun updateState() {

        var status = serviceStatusRepository.subscribeOnStatus().value
        if (status == null) status = ServiceStatus.STOPPED

        if (canDrawOverlay())
            when (status) {
                ServiceStatus.STOPPED -> mutableState.setValue(ServiceManagerStates.STOPPED)
                ServiceStatus.LAUNCHING -> mutableState.setValue(ServiceManagerStates.LAUNCHING)
                ServiceStatus.RUNNING -> mutableState.setValue(ServiceManagerStates.RUNNING)
            }
        else
            mutableState.setValue(ServiceManagerStates.CANT_DRAW_OVERLAY)

    }


    fun subscribeOnServiceStatus(): LiveData<ServiceManagerStates> {
        return mutableState
    }

}
