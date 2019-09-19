package ua.syt0r.furiganit.model.repository.hisotry.local.implementation

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ua.syt0r.furiganit.model.db.HistoryDao
import ua.syt0r.furiganit.model.db.HistoryDatabase
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryItemMapper
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository

class LocalHistoryRepositoryImpl(
    database: HistoryDatabase,
    private val mapper: LocalHistoryItemMapper
) : LocalHistoryRepository {

    private val historyDao: HistoryDao = database.historyDao

    override fun add(historyItem: HistoryItem) =
        historyDao.add(mapper.toLocalHistoryItem(historyItem))

    override fun addAll(history: List<HistoryItem>) =
        historyDao.addAll(history.map(mapper::toLocalHistoryItem))

    override fun remove(historyItem: HistoryItem): Completable {
        return Single.just(historyItem)
            .map(mapper::toLocalHistoryItem)
            .flatMapCompletable { historyDao.remove(it) }
    }

    override fun removeAll() = historyDao.removeAll()

    override fun fetchHistory(): Flowable<List<HistoryItem>> {
        return historyDao.fetchHistory()
            .map { it.map(mapper::toHistoryItem).toMutableList() }
    }

}
