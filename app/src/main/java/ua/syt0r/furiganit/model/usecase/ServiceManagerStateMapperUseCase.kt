package ua.syt0r.furiganit.model.usecase

import ua.syt0r.furiganit.app.serviceManager.ServiceManagerState
import ua.syt0r.furiganit.model.repository.status.ServiceState

interface ServiceManagerStateMapperUseCase {
    fun translate(canDrawOverlay: Boolean, serviceState: ServiceState): ServiceManagerState
}