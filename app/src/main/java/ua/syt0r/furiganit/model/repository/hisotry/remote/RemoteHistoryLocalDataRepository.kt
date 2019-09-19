package ua.syt0r.furiganit.model.repository.hisotry.remote

import io.reactivex.Completable

interface RemoteHistoryLocalDataRepository {
    fun insert(remoteHistoryItem: RemoteHistoryItem): Completable
    fun update(remoteHistoryItem: RemoteHistoryItem): Completable
    fun delete(remoteHistoryItem: RemoteHistoryItem): Completable
}