package ua.syt0r.furiganit.app.main.screens.service_manager

import androidx.annotation.StringRes
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.core.service.ServiceState

enum class ServiceManagerState(
    @StringRes val buttonTextResId: Int,
    @StringRes val hintTextResId: Int
) {
    STOPPED(R.string.service_manager_start_state_button, R.string.service_manager_start_state_hint),
    LAUNCHING(R.string.service_manager_starting_state_button, R.string.service_manager_starting_state_hint),
    RUNNING(R.string.service_manager_stop_state_button, R.string.service_manager_stop_state_hint)
}

fun ServiceState.toServiceManagerState(): ServiceManagerState {
    return when {
        this == ServiceState.STOPPED -> ServiceManagerState.STOPPED
        this == ServiceState.LAUNCHING -> ServiceManagerState.LAUNCHING
        this == ServiceState.RUNNING -> ServiceManagerState.RUNNING
        else -> throw IllegalStateException("Unknown State")
    }
}