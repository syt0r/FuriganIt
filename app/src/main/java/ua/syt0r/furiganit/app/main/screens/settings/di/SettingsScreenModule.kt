package ua.syt0r.furiganit.app.main.screens.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.syt0r.furiganit.app.main.screens.settings.SettingsViewModel

val settingsScreenModule = module {
    viewModel { SettingsViewModel() }
}