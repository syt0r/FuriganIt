package ua.syt0r.furiganit.core.overlay.di

import android.content.Context
import android.view.WindowManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.syt0r.furiganit.core.overlay.OverlayDataRepository
import ua.syt0r.furiganit.core.overlay.OverlayManager

val overlayModule = module {

    single { androidContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    single { OverlayDataRepository(androidContext()) }

    single { OverlayManager(androidContext(), get(), get()) }

}