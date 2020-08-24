package ua.syt0r.furiganit.core.app_data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.syt0r.furiganit.core.app_data.AppDataRepository

val appDataModule = module {

    single { AppDataRepository(androidContext()) }

}