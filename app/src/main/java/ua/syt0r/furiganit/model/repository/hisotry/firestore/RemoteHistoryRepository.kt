package ua.syt0r.furiganit.model.repository.hisotry.firestore

import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.entity.UserData

interface RemoteHistoryRepository {
    fun createUser(): Single<UserData>
    fun sync(history: List<HistoryItem>): Completable
}