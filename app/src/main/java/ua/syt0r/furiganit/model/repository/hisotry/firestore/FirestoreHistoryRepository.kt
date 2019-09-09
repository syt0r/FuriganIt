package ua.syt0r.furiganit.model.repository.hisotry.firestore

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.entity.UserData
import ua.syt0r.furiganit.model.repository.user.UserRepository

class FirestoreHistoryRepository(
        private val userRepository: UserRepository
) : RemoteHistoryRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun createUser() = Single.create<UserData> { emitter ->

    }

    override fun sync(history: List<HistoryItem>) = Completable.create{ emitter ->

        firestore.collection(HISTORY_COLLECTION).document("").get()

    }

    companion object {
        private const val USER_DATA_COLLECTION = "user_data"
        private const val HISTORY_COLLECTION = "history"
    }

}

