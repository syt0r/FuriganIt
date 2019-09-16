package ua.syt0r.furiganit.model.repository.hisotry.local

import ua.syt0r.furiganit.model.db.HistoryDao
import ua.syt0r.furiganit.model.db.HistoryDatabase
import ua.syt0r.furiganit.model.entity.HistoryItem

class LocalHistoryRepositoryImpl(database: HistoryDatabase) : LocalHistoryRepository {

    private val historyDao: HistoryDao = database.historyDao

    override fun add(historyItem: HistoryItem) = historyDao.add(historyItem)
    override fun addAll(history: List<HistoryItem>) = historyDao.addAll(history)

    override fun remove(historyItem: HistoryItem) = historyDao.remove(historyItem)
    override fun removeAll() = historyDao.removeAll()

    override fun fetchHistory() = historyDao.fetchHistory()

}
