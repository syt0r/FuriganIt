package ua.syt0r.furiganit.app.furigana.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.syt0r.furiganit.app.furigana.FuriganaViewModel

val furiganaScreenModule = module {
    viewModel { FuriganaViewModel(get(), get(), get()) }
}