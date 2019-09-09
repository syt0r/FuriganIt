package ua.syt0r.furiganit.model.repository.hisotry.local

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.HistoryItem

interface LocalHistoryRepository {
    fun add(historyItem: HistoryItem): Completable
    fun remove(historyItem: HistoryItem): Completable
    fun fetchHistory(): Flowable<List<HistoryItem>>
}
