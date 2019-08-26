package ua.syt0r.furiganit.model.repository.hisotry.firestore;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;
import ua.syt0r.furiganit.model.entity.HistoryItem;
import ua.syt0r.furiganit.model.repository.hisotry.HistoryRepository;

public class FirestoreHistoryRepository implements HistoryRepository {

    private static final String HISTORY_COLLECTION = "history";

    private FirebaseFirestore firestore;

    public FirestoreHistoryRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public Completable add(HistoryItem historyItem) {
        return Completable.create(emitter -> firestore.collection(HISTORY_COLLECTION)
                .add(historyItem)
                .addOnSuccessListener(documentReference -> emitter.onComplete())
                .addOnFailureListener(emitter::onError));
    }

    @Override
    public Completable remove(HistoryItem historyItem) {
        return Completable.create(emitter -> firestore.collection(HISTORY_COLLECTION)
                .document("")
                .delete()
                .addOnCompleteListener(task -> emitter.onComplete())
                .addOnFailureListener(emitter::onError));
    }

    @Override
    public Single<List<HistoryItem>> fetchHistory() {
        return Single.create(emitter -> firestore.collection(HISTORY_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->
                        emitter.onSuccess(queryDocumentSnapshots.toObjects(HistoryItem.class)))
                .addOnFailureListener(emitter::onError));
    }

}
