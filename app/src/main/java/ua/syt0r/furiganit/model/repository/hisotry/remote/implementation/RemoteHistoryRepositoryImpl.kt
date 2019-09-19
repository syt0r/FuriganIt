package ua.syt0r.furiganit.model.repository.hisotry.remote.implementation

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.remote.RemoteHistoryRepository
import ua.syt0r.furiganit.model.repository.hisotry.remote.RepoState
import ua.syt0r.furiganit.model.repository.hisotry.remote.SyncAction
import ua.syt0r.furiganit.model.repository.userData.UserDataRepository
import java.lang.IllegalStateException

class RemoteHistoryRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val userDataRepository: UserDataRepository,
    private val localHistoryRepository: LocalHistoryRepository
) : RemoteHistoryRepository {

    //TODO compare local and remote storage data by update time
    //TODO allow user to select one of actions: merge, overwrite local or remote data storage3

    override fun checkRemoteRepo() = userDataRepository.getUserData().map {

        val localUpdateTime = it.update_time
        val remoteUpdateTime = firestore.collection(USER_DATA_COLLECTION)
            .whereEqualTo(USER_DATA_USER_FIELD, it.user).get()

        when {
            localUpdateTime == null -> RepoState.NEWER
            remoteUpdateTime == null -> RepoState.OUTDATED
            else -> throw IllegalStateException("")
        }

    }

    override fun sync(history: List<HistoryItem>, syncAction: SyncAction): Completable {

        return userDataRepository.getUserData()
            .flatMapCompletable { userData ->
                Completable.fromRunnable {

                    val userId = userData.user

                    when (syncAction) {

                        SyncAction.MERGE -> this.firestore.runTransaction {


                        }

                        SyncAction.OVERWRITE_LOCAL -> this.firestore.runTransaction {
                            val remoteHistory = fetchRemoteHistory(userId)
                            localHistoryRepository.removeAll()
                            localHistoryRepository.addAll(remoteHistory)
                        }

                        SyncAction.OVERWRITE_REMOTE -> this.firestore.runTransaction {

                            clearUserHistory(userId)
                            insertHistory(userId, history)

                        }

                    }

                }

            }

    }


    private fun clearUserHistory(userId: String) {

        this.firestore.collection(HISTORY_COLLECTION)
            .whereEqualTo(HISTORY_COLLECTION_USER_FIELD, userId)


    }

    private fun insertHistory(userId: String, history: List<HistoryItem>) {

        for (historyItem in history) {
            this.firestore.collection(HISTORY_COLLECTION)
                .add(historyItem)
        }

    }

    private fun fetchRemoteHistory(userId: String): List<HistoryItem> {

        val documents = this.firestore.collection(HISTORY_COLLECTION)
            .whereEqualTo(HISTORY_COLLECTION_USER_FIELD, userId)
            .get()
            .result
            ?.documents

        val history = ArrayList<HistoryItem>(documents?.size ?: 0)

        documents?.forEach { docSnap ->
            docSnap.toObject(HistoryItem::class.java)?.also { history.add(it) }
        }

        return history
    }

    private fun updateUserDataCollection(userId: String) {

    }


    companion object {

        private const val USER_DATA_COLLECTION = "user_data"
        private const val HISTORY_COLLECTION = "history"

        private const val USER_DATA_USER_FIELD = "user"
        private const val HISTORY_COLLECTION_USER_FIELD = "user"

    }

}

