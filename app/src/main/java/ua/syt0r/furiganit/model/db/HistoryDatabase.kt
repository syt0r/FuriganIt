package ua.syt0r.furiganit.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryItem

@Database(entities = [LocalHistoryItem::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {

    abstract val historyDao: HistoryDao

    companion object {

        private const val DATABASE_NAME = "history_db"

        fun create(context: Context): HistoryDatabase = Room
                .databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        DATABASE_NAME)
                .build()


    }

}
