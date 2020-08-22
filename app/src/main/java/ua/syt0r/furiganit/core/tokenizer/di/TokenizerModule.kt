package ua.syt0r.furiganit.core.tokenizer.di

import org.koin.dsl.module
import ua.syt0r.furiganit.core.tokenizer.TokenizerWrapper

val tokenizerModule = module {
    single { TokenizerWrapper() }
}