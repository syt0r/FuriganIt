package ua.syt0r.furiganit.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ua.syt0r.furiganit.model.entity.HistoryItem

@Dao
interface HistoryDao {

    @Insert
    fun add(item: HistoryItem)

    @Delete
    fun remove(item: HistoryItem)

    @Query("SELECT * FROM historyitem")
    fun fetchHistory(): List<HistoryItem>

}
