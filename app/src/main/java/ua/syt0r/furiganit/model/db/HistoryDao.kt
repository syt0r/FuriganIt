package ua.syt0r.furiganit.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryItem

@Dao
interface HistoryDao {

    @Insert
    fun add(item: LocalHistoryItem): Completable

    @Insert
    fun addAll(history: List<LocalHistoryItem>): Completable

    @Delete
    fun remove(item: LocalHistoryItem): Completable

    @Query("DELETE FROM history_items")
    fun removeAll(): Completable

    @Query("SELECT * FROM history_items")
    fun fetchHistory(): Flowable<List<LocalHistoryItem>>

}
