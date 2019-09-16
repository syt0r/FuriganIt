package ua.syt0r.furiganit.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import ua.syt0r.furiganit.model.entity.HistoryItem

@Dao
interface HistoryDao {

    @Insert
    fun add(item: HistoryItem): Completable

    @Insert
    fun addAll(history: List<HistoryItem>): Completable

    @Delete
    fun remove(item: HistoryItem): Completable

    @Query("DELETE FROM historyitem")
    fun removeAll(): Completable

    @Query("SELECT * FROM historyitem")
    fun fetchHistory(): Flowable<List<HistoryItem>>

}
