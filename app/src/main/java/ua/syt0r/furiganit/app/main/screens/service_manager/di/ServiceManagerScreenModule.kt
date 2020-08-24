package ua.syt0r.furiganit.app.main.screens.service_manager.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.syt0r.furiganit.app.main.screens.service_manager.ServiceManagerViewModel

val serviceManagerScreenModule = module {
    viewModel { ServiceManagerViewModel(get()) }
}