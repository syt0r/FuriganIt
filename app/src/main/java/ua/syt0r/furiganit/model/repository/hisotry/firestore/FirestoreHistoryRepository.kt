package ua.syt0r.furiganit.model.repository.hisotry.firestore

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.HistoryRepository

class FirestoreHistoryRepository : HistoryRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun add(historyItem: HistoryItem): Completable {
        return Completable.create { emitter ->
            firestore.collection(HISTORY_COLLECTION)
                    .add(historyItem)
                    .addOnSuccessListener { documentReference -> emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun remove(historyItem: HistoryItem): Completable {
        return Completable.create { emitter ->
            firestore.collection(HISTORY_COLLECTION)
                    .document("")
                    .delete()
                    .addOnCompleteListener { task -> emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun fetchHistory(): Single<List<HistoryItem>> {
        return Single.create { emitter ->
            firestore.collection(HISTORY_COLLECTION)
                    .get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        emitter.onSuccess(queryDocumentSnapshots.toObjects(HistoryItem::class.java))
                    }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    companion object {
        private const val HISTORY_COLLECTION = "history"
    }

}

