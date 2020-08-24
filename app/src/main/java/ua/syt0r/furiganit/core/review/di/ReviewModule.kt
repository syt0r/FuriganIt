package ua.syt0r.furiganit.core.review.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.syt0r.furiganit.core.review.ReviewManager

val reviewModule = module {

    single { ReviewManager(androidContext(), get()) }

}