package ua.syt0r.furiganit.app.main.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.syt0r.furiganit.app.main.MainViewModel

val mainScreenModule = module {
    viewModel { MainViewModel(get()) }
}