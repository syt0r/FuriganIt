package ua.syt0r.furiganit.model.repository.hisotry.local

import android.content.Context
import androidx.room.Room
import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.db.HistoryDao
import ua.syt0r.furiganit.model.db.HistoryDatabase
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.HistoryRepository

class LocalHistoryRepository(context: Context) : HistoryRepository {

    private val historyDao: HistoryDao

    init {

        val database = Room.databaseBuilder(context.applicationContext,
                HistoryDatabase::class.java, DATABASE_NAME).build()

        historyDao = database.historyDao

    }

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

    override fun fetchHistory(): Single<List<HistoryItem>> {
        return Single.create { emitter -> emitter.onSuccess(historyDao.fetchHistory()) }
    }

    companion object {
        private const val DATABASE_NAME = "history_db"
    }

}
