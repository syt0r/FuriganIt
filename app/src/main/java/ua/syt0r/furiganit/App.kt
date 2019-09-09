package ua.syt0r.furiganit

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ua.syt0r.furiganit.app.about.AboutViewModel
import ua.syt0r.furiganit.app.about.BillingInteractor
import ua.syt0r.furiganit.app.about.BillingUseCase
import ua.syt0r.furiganit.app.furigana.FuriganaViewModel
import ua.syt0r.furiganit.app.history.HistoryViewModel
import ua.syt0r.furiganit.app.serviceManager.interactor.OverlayDrawabilityCheckInteractor
import ua.syt0r.furiganit.app.serviceManager.useCase.OverlayDrawabilityCheckUseCase
import ua.syt0r.furiganit.app.serviceManager.ServiceManagerViewModel
import ua.syt0r.furiganit.app.serviceManager.interactor.ServiceManagerStateMapperInteractor
import ua.syt0r.furiganit.app.serviceManager.useCase.ServiceManagerStateMapperUseCase
import ua.syt0r.furiganit.model.db.HistoryDatabase
import ua.syt0r.furiganit.model.repository.hisotry.firestore.FirestoreHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.firestore.RemoteHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.RoomHistoryRepository
import ua.syt0r.furiganit.model.repository.overlay.OverlayDataRepository
import ua.syt0r.furiganit.model.repository.status.ServiceStateRepository
import ua.syt0r.furiganit.model.repository.user.SharedPreferencesUserRepository
import ua.syt0r.furiganit.model.repository.user.UserRepository

class App : Application() {

    private val viewModelModule = module {

        viewModel { AboutViewModel(get()) }
        viewModel { HistoryViewModel() }
        viewModel { ServiceManagerViewModel(get(), get(), get()) }
        viewModel { FuriganaViewModel(get()) }

    }

    private val useCaseModule = module {

        factory<BillingUseCase> { BillingInteractor(get()) }
        factory<OverlayDrawabilityCheckUseCase> { OverlayDrawabilityCheckInteractor(get()) }
        factory<ServiceManagerStateMapperUseCase> { ServiceManagerStateMapperInteractor() }

    }

    private val repositoryModule = module {

        factory { OverlayDataRepository(get()) }
        factory { ServiceStateRepository(get()) }

        single { HistoryDatabase.create(get()) }
        factory { RoomHistoryRepository(get()) }

        factory<UserRepository> { SharedPreferencesUserRepository(get()) }
        factory<RemoteHistoryRepository> { FirestoreHistoryRepository(get()) }

    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(repositoryModule, useCaseModule, viewModelModule))
        }

    }

}
