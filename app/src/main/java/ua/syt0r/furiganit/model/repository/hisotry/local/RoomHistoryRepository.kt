package ua.syt0r.furiganit.model.repository.hisotry.local

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import ua.syt0r.furiganit.model.db.HistoryDao
import ua.syt0r.furiganit.model.db.HistoryDatabase
import ua.syt0r.furiganit.model.entity.HistoryItem

class RoomHistoryRepository(database: HistoryDatabase) : LocalHistoryRepository {

    private val historyDao: HistoryDao = database.historyDao

    override fun add(historyItem: HistoryItem): Completable {
        return Completable.create { emitter ->
            historyDao.add(historyItem)
            emitter.onComplete()
        }
    }

    override fun remove(historyItem: HistoryItem): Completable {
        return Completable.create { emitter ->
            historyDao.remove(historyItem)
            emitter.onComplete()
        }
    }

    override fun fetchHistory(): Flowable<List<HistoryItem>> {
        return historyDao.fetchHistory()
    }

}
