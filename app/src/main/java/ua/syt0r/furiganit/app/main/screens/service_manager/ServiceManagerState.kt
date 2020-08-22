package ua.syt0r.furiganit.app.main.screens.service_manager

import androidx.annotation.StringRes
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.core.service.ServiceState

enum class ServiceManagerState(
        @StringRes val buttonTextResId: Int,
        @StringRes val hintTextResId: Int
) {
    CANT_DRAW_OVERLAY(R.string.go_to_settings, R.string.got_to_sett_hint),
    STOPPED(R.string.start, R.string.start_hint),
    LAUNCHING(R.string.starting, R.string.starting_hint),
    RUNNING(R.string.stop, R.string.stop_hint)
}

fun ServiceState.toServiceManagerState(canDrawOverlay: Boolean): ServiceManagerState {
    return when {
        !canDrawOverlay -> ServiceManagerState.CANT_DRAW_OVERLAY
        this == ServiceState.STOPPED -> ServiceManagerState.STOPPED
        this == ServiceState.LAUNCHING -> ServiceManagerState.LAUNCHING
        this == ServiceState.RUNNING -> ServiceManagerState.RUNNING
        else -> throw IllegalStateException("Unknown State")
    }
}