package ua.syt0r.furiganit.app.main.screens.about.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.syt0r.furiganit.app.main.screens.about.AboutViewModel

val aboutScreenModule = module {
    viewModel { AboutViewModel() }
}