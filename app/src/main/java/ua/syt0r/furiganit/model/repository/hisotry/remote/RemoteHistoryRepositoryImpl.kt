package ua.syt0r.furiganit.model.repository.hisotry.remote

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.entity.UserData
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryRepository
import ua.syt0r.furiganit.model.repository.userData.UserDataRepository

class RemoteHistoryRepositoryImpl(
        private val userDataRepository: UserDataRepository,
        private val localHistoryRepository: LocalHistoryRepository
) : RemoteHistoryRepository {

    //TODO compare local and remote storage data by update time
    //TODO allow user to select one of actions: merge, overwrite local or remote data storage3

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun createUser() = Single.create<UserData> { emitter ->

    }

    override fun checkRemoteRepo() = Single.create<RepoState> { emitter ->
        emitter.onSuccess(RepoState.OUTDATED)
    }

    override fun sync(history: List<HistoryItem>, syncAction: SyncAction): Completable {

        return userDataRepository.getUserData()
                .flatMapCompletable { userData -> Completable.fromRunnable {

                        val userId = userData.user

                        when(syncAction) {

                            SyncAction.MERGE -> firestore.runTransaction {



                            }

                            SyncAction.OVERWRITE_LOCAL -> firestore.runTransaction {
                                val remoteHistory = fetchRemoteHistory(userId)
                                localHistoryRepository.removeAll()
                                localHistoryRepository.addAll(remoteHistory)
                            }

                            SyncAction.OVERWRITE_REMOTE -> firestore.runTransaction {

                                clearUserHistory(userId)
                                insertHistory(userId, history)

                            }

                        }

                    }

                }

    }


    private fun clearUserHistory(userId: String) {

        firestore.collection(HISTORY_COLLECTION)
                .whereEqualTo(HISTORY_COLLECTION_USER_FIELD, userId)


    }

    private fun insertHistory(userId: String, history: List<HistoryItem>) {

        for (historyItem in history) {
            firestore.collection(HISTORY_COLLECTION)
                    .add(historyItem)
        }

    }

    private fun fetchRemoteHistory(userId: String): List<HistoryItem> {

        val documents = firestore.collection(HISTORY_COLLECTION)
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

        private const val HISTORY_COLLECTION_USER_FIELD = "user"

    }

}

