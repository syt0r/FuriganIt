package ua.syt0r.furiganit.app.about

import io.reactivex.Completable
import io.reactivex.Observable

interface BillingUseCase {
    fun isAvailable(): Observable<Boolean>
    fun purchase(): Completable
    fun connect()
    fun disconnect()
}