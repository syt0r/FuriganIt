package ua.syt0r.furiganit

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ua.syt0r.furiganit.app.about.AboutViewModel
import ua.syt0r.furiganit.app.about.BillingManager
import ua.syt0r.furiganit.app.furigana.FuriganaViewModel
import ua.syt0r.furiganit.app.history.HistoryViewModel
import ua.syt0r.furiganit.model.usecase.implementation.OverlayDrawabilityCheckUseCaseImpl
import ua.syt0r.furiganit.model.usecase.OverlayDrawabilityCheckUseCase
import ua.syt0r.furiganit.app.serviceManager.ServiceManagerViewModel
import ua.syt0r.furiganit.model.usecase.implementation.ServiceManagerStateMapperUseCaseImpl
import ua.syt0r.furiganit.model.usecase.ServiceManagerStateMapperUseCase
import ua.syt0r.furiganit.model.db.HistoryDatabase
import ua.syt0r.furiganit.model.repository.hisotry.remote.RemoteHistoryRepositoryImpl
import ua.syt0r.furiganit.model.repository.hisotry.remote.RemoteHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepositoryImpl
import ua.syt0r.furiganit.model.repository.overlay.OverlayDataRepository
import ua.syt0r.furiganit.model.repository.status.ServiceStateRepository
import ua.syt0r.furiganit.model.repository.user.SharedPreferencesUserRepository
import ua.syt0r.furiganit.model.repository.user.UserRepository
import ua.syt0r.furiganit.model.usecase.TextLocalizerUseCase
import ua.syt0r.furiganit.model.usecase.implementation.TextLocalizerUseCaseImpl

class App : Application() {

    private val repositoryModule = module {

        single { HistoryDatabase.create(get()) }

        factory { OverlayDataRepository(get()) }
        factory { ServiceStateRepository(get()) }
        factory { LocalHistoryRepositoryImpl(get()) }
        factory<UserRepository> { SharedPreferencesUserRepository(get()) }
        factory<RemoteHistoryRepository> { RemoteHistoryRepositoryImpl(get(), get()) }

    }

    private val useCaseModule = module {

        single<TextLocalizerUseCase> { TextLocalizerUseCaseImpl(get()) }
        factory<OverlayDrawabilityCheckUseCase> { OverlayDrawabilityCheckUseCaseImpl(get()) }
        factory<ServiceManagerStateMapperUseCase> { ServiceManagerStateMapperUseCaseImpl() }

    }

    private val viewModelModule = module {

        viewModel { HistoryViewModel(get(), get(), get()) }
        viewModel { ServiceManagerViewModel(get(), get(), get()) }
        viewModel { FuriganaViewModel(get()) }

    }

    private val aboutModule = module {

        single { BillingManager(get()) }
        viewModel { AboutViewModel(get(), get()) }

    }


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                    listOf(
                            repositoryModule,
                            useCaseModule,
                            viewModelModule,
                            aboutModule
                    )
            )
        }

    }

}
