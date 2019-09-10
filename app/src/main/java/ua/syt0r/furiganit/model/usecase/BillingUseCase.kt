package ua.syt0r.furiganit.model.usecase

import io.reactivex.Completable
import io.reactivex.Observable

interface BillingUseCase {
    fun isAvailable(): Observable<Boolean>
    fun purchase(): Completable
    fun connect()
    fun disconnect()
}