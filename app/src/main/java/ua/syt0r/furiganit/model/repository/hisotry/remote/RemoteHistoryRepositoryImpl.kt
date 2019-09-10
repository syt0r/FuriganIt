package ua.syt0r.furiganit.model.repository.hisotry.remote

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.entity.UserData
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.model.repository.user.UserRepository

class RemoteHistoryRepositoryImpl(
        private val userRepository: UserRepository,
        private val localHistoryRepository: LocalHistoryRepository
) : RemoteHistoryRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun createUser() = Single.create<UserData> { emitter ->

    }

    //TODO compare local and remote storage data by update time
    //TODO allow user to select one of actions: merge, overwrite local or remote data storage

    override fun sync(history: List<HistoryItem>) = Completable.create{ emitter ->

        firestore.collection(HISTORY_COLLECTION).document("").get()

    }

    companion object {
        private const val USER_DATA_COLLECTION = "user_data"
        private const val HISTORY_COLLECTION = "history"
    }

}

