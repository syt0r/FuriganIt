package ua.syt0r.furiganit.model.usecase

import ua.syt0r.furiganit.app.serviceManager.ServiceManagerState
import ua.syt0r.furiganit.model.repository.serviceState.ServiceState

interface ServiceManagerStateMapper {
    fun map(canDrawOverlay: Boolean, serviceState: ServiceState): ServiceManagerState
}