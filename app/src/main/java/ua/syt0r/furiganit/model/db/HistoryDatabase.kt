package ua.syt0r.furiganit.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

import ua.syt0r.furiganit.model.entity.HistoryItem

@Database(entities = [HistoryItem::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract val historyDao: HistoryDao
}
