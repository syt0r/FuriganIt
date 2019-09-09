package ua.syt0r.furiganit.app.serviceManager.interactor

import ua.syt0r.furiganit.app.serviceManager.ServiceManagerState
import ua.syt0r.furiganit.app.serviceManager.useCase.ServiceManagerStateMapperUseCase
import ua.syt0r.furiganit.model.repository.status.ServiceState
import java.lang.IllegalStateException

class ServiceManagerStateMapperInteractor : ServiceManagerStateMapperUseCase {

    override fun translate(canDrawOverlay: Boolean, serviceState: ServiceState): ServiceManagerState {
        return when {
            !canDrawOverlay -> ServiceManagerState.CANT_DRAW_OVERLAY
            serviceState == ServiceState.STOPPED -> ServiceManagerState.STOPPED
            serviceState == ServiceState.LAUNCHING -> ServiceManagerState.LAUNCHING
            serviceState == ServiceState.RUNNING -> ServiceManagerState.RUNNING
            else -> throw IllegalStateException("Unknown State")
        }
    }

}