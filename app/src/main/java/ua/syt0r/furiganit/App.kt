package ua.syt0r.furiganit

import android.app.Application
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ua.syt0r.furiganit.app.about.AboutViewModel
import ua.syt0r.furiganit.app.about.BillingManager
import ua.syt0r.furiganit.app.furigana.FuriganaViewModel
import ua.syt0r.furiganit.app.history.HistoryViewModel
import ua.syt0r.furiganit.model.usecase.implementation.OverlayDrawabilityCheckerImpl
import ua.syt0r.furiganit.model.usecase.OverlayDrawabilityChecker
import ua.syt0r.furiganit.app.serviceManager.ServiceManagerViewModel
import ua.syt0r.furiganit.app.settings.SettingsViewModel
import ua.syt0r.furiganit.model.usecase.implementation.ServiceManagerStateMapperImpl
import ua.syt0r.furiganit.model.usecase.ServiceManagerStateMapper
import ua.syt0r.furiganit.model.db.HistoryDatabase
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryItemMapper
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.implementation.LocalHistoryItemMapperImpl
import ua.syt0r.furiganit.model.repository.hisotry.remote.implementation.RemoteHistoryRepositoryImpl
import ua.syt0r.furiganit.model.repository.hisotry.remote.RemoteHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.local.implementation.LocalHistoryRepositoryImpl
import ua.syt0r.furiganit.model.repository.overlay.OverlayDataRepository
import ua.syt0r.furiganit.model.repository.serviceState.ServiceStateRepository
import ua.syt0r.furiganit.model.repository.userData.UserDataRepositoryImpl
import ua.syt0r.furiganit.model.repository.userData.UserDataRepository
import ua.syt0r.furiganit.model.usecase.TextLocalizer
import ua.syt0r.furiganit.model.usecase.implementation.TextLocalizerImpl

class App : Application() {

    private val otherDependenciesModule = module {

        // Auth

        single {
            FirebaseApp.initializeApp(get())
            FirebaseAuth.getInstance()
        }

        single { AuthUI.getInstance() }

        // Billing

        single { BillingManager(get()) }

        //Firestore

        single { FirebaseFirestore.getInstance() }

        // Mappers

        single<LocalHistoryItemMapper> { LocalHistoryItemMapperImpl() }

    }

    private val repositoryModule = module {

        single { HistoryDatabase.create(get()) }

        factory { OverlayDataRepository(get()) }
        factory { ServiceStateRepository(get()) }

        factory<LocalHistoryRepository> { LocalHistoryRepositoryImpl(get(), get()) }
        factory<UserDataRepository> { UserDataRepositoryImpl(get()) }
        factory<RemoteHistoryRepository> { RemoteHistoryRepositoryImpl(get(), get(), get()) }

    }

    private val useCaseModule = module {

        single<TextLocalizer> { TextLocalizerImpl(get()) }
        factory<OverlayDrawabilityChecker> { OverlayDrawabilityCheckerImpl(get()) }

        single<ServiceManagerStateMapper> { ServiceManagerStateMapperImpl() }

    }

    private val viewModelModule = module {

        viewModel { ServiceManagerViewModel(get(), get(), get()) }
        viewModel { HistoryViewModel(get(), get(), get(), get()) }
        viewModel { SettingsViewModel(get(), get(), get()) }
        viewModel { AboutViewModel(get(), get()) }
        viewModel { FuriganaViewModel(get()) }

    }


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    otherDependenciesModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }

    }

}
