package ua.syt0r.furiganit.model.repository.hisotry.local;

import android.content.Context;
import androidx.room.Room;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ua.syt0r.furiganit.model.db.HistoryDao;
import ua.syt0r.furiganit.model.db.HistoryDatabase;
import ua.syt0r.furiganit.model.entity.HistoryItem;
import ua.syt0r.furiganit.model.repository.hisotry.HistoryRepository;

public class LocalHistoryRepository implements HistoryRepository {

    private static final String DATABASE_NAME = "history_db";

    private HistoryDao historyDao;

    public LocalHistoryRepository(Context context) {

        HistoryDatabase database = Room.databaseBuilder(context.getApplicationContext(),
                HistoryDatabase.class, DATABASE_NAME).build();

        historyDao = database.getHistoryDao();

    }

    @Override
    public Completable add(HistoryItem historyItem) {
        return Completable.create(emitter -> {
            historyDao.add(historyItem);
            emitter.onComplete();
        });
    }

    @Override
    public Completable remove(HistoryItem historyItem) {
        return Completable.create(emitter -> {
            historyDao.remove(historyItem);
            emitter.onComplete();
        });
    }

    @Override
    public Single<List<HistoryItem>> fetchHistory() {
        return Single.create(emitter -> emitter.onSuccess(historyDao.fetchHistory()));
    }

}
