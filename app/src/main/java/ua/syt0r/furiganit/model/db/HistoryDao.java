package ua.syt0r.furiganit.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import ua.syt0r.furiganit.model.entity.HistoryItem;

@Dao
public interface HistoryDao {

    @Insert
    void add(HistoryItem item);

    @Delete
    void remove(HistoryItem item);

    @Query("SELECT * FROM historyitem")
    List<HistoryItem> fetchHistory();

}
