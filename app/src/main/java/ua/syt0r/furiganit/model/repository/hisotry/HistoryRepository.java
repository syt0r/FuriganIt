package ua.syt0r.furiganit.model.repository.hisotry;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;
import ua.syt0r.furiganit.model.entity.HistoryItem;

public interface HistoryRepository {
    Completable add(HistoryItem historyItem);
    Completable remove(HistoryItem historyItem);
    Single<List<HistoryItem>> fetchHistory();
}
