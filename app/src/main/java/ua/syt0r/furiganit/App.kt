package ua.syt0r.furiganit

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ua.syt0r.furiganit.model.repository.hisotry.firestore.FirestoreHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.model.repository.overlay.OverlayDataRepository
import ua.syt0r.furiganit.model.repository.status.ServiceStatusRepository

class App : Application() {

    private val repositoryModule = module {

        factory { FirestoreHistoryRepository() }
        factory { LocalHistoryRepository(this@App) }
        factory { OverlayDataRepository(this@App) }
        factory { ServiceStatusRepository(this@App) }

    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(repositoryModule)
        }

    }

}
