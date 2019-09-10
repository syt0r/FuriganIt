package ua.syt0r.furiganit.model.usecase.implementation

import android.content.Context
import android.provider.Settings
import ua.syt0r.furiganit.model.usecase.OverlayDrawabilityCheckUseCase

class OverlayDrawabilityCheckUseCaseImpl(private val context: Context) : OverlayDrawabilityCheckUseCase {

    override fun canDrawOverlay(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            Settings.canDrawOverlays(context)
        else true
    }

}