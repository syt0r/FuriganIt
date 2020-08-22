package ua.syt0r.furiganit.core.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object DiInjector {

    fun inject(app: Application) {
        startKoin {
            androidContext(app)
            modules(applicationModules.toList())
        }
    }

}