package ua.syt0r.furiganit.core.di

import ua.syt0r.furiganit.app.furigana.di.furiganaScreenModule
import ua.syt0r.furiganit.app.main.screens.about.di.aboutScreenModule
import ua.syt0r.furiganit.app.main.screens.service_manager.di.serviceManagerScreenModule
import ua.syt0r.furiganit.app.main.screens.settings.di.settingsScreenModule
import ua.syt0r.furiganit.core.clipboard.di.clipboardHandlerModule
import ua.syt0r.furiganit.core.notification_manager.di.notificationManagerModule
import ua.syt0r.furiganit.core.overlay.di.overlayModule
import ua.syt0r.furiganit.core.service.di.serviceModule
import ua.syt0r.furiganit.core.tokenizer.di.tokenizerModule


val coreModules = setOf(
        notificationManagerModule,
        clipboardHandlerModule,
        overlayModule,
        serviceModule,
        tokenizerModule
)

val screenModules = setOf(
        aboutScreenModule,
        serviceManagerScreenModule,
        settingsScreenModule,
        furiganaScreenModule
)

val applicationModules = coreModules.union(screenModules)