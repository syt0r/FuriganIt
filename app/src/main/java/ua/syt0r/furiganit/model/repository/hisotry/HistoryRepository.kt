package ua.syt0r.furiganit.model.repository.hisotry

import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.HistoryItem

interface HistoryRepository {
    fun add(historyItem: HistoryItem): Completable
    fun remove(historyItem: HistoryItem): Completable
    fun fetchHistory(): Single<List<HistoryItem>>
}
