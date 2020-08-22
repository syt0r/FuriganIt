package ua.syt0r.furiganit.core.service.di

import org.koin.dsl.module
import ua.syt0r.furiganit.core.service.ServiceStateObservable

val serviceModule = module {
    factory { ServiceStateObservable(get()) }
}