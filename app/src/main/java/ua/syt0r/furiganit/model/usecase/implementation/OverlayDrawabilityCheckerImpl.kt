package ua.syt0r.furiganit.model.usecase.implementation

import android.content.Context
import android.provider.Settings
import ua.syt0r.furiganit.model.usecase.OverlayDrawabilityChecker

class OverlayDrawabilityCheckerImpl(private val context: Context) : OverlayDrawabilityChecker {

    override fun canDrawOverlay(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            Settings.canDrawOverlays(context)
        else true
    }

}