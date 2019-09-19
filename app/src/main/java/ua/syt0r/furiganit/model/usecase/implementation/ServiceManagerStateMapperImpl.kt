package ua.syt0r.furiganit.model.usecase.implementation

import ua.syt0r.furiganit.app.serviceManager.ServiceManagerState
import ua.syt0r.furiganit.model.usecase.ServiceManagerStateMapper
import ua.syt0r.furiganit.model.repository.serviceState.ServiceState
import java.lang.IllegalStateException

class ServiceManagerStateMapperImpl : ServiceManagerStateMapper {

    override fun map(canDrawOverlay: Boolean, serviceState: ServiceState): ServiceManagerState {
        return when {
            !canDrawOverlay -> ServiceManagerState.CANT_DRAW_OVERLAY
            serviceState == ServiceState.STOPPED -> ServiceManagerState.STOPPED
            serviceState == ServiceState.LAUNCHING -> ServiceManagerState.LAUNCHING
            serviceState == ServiceState.RUNNING -> ServiceManagerState.RUNNING
            else -> throw IllegalStateException("Unknown State")
        }
    }

}