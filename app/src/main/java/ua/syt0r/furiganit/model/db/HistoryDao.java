package ua.syt0r.furiganit.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ua.syt0r.furiganit.model.entity.HistoryItem;

@Dao
public interface HistoryDao {

    @Insert
    Completable add(HistoryItem item);

    @Delete
    Completable remove(HistoryItem item);

    @Query("SELECT * FROM historyitem")
    Single<List<HistoryItem>> fetchHistory();

}
