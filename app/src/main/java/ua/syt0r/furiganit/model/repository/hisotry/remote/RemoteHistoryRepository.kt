package ua.syt0r.furiganit.model.repository.hisotry.remote

import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.entity.UserData

interface RemoteHistoryRepository {
    fun createUser(): Single<UserData>
    fun checkRemoteRepo(): Single<SyncAction>
    fun sync(history: List<HistoryItem>, syncAction: SyncAction): Completable
}