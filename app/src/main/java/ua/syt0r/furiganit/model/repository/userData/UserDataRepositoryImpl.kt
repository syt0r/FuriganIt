package ua.syt0r.furiganit.model.repository.userData

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.UserData

class UserDataRepositoryImpl(context: Context) : UserDataRepository {

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun saveUserData(userData: UserData) = Completable.create { emitter ->
        sharedPreferences.edit().putString(USER_DATA_KEY, Gson().toJson(userData))
        emitter.onComplete()
    }

    override fun getUserData() = Single.fromCallable<UserData> {
        Gson().fromJson(
                sharedPreferences.getString(USER_DATA_KEY, ""),
                UserData::class.java
        )
    }

    override fun clearUserData(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val PREF_NAME = "user_data"
        private const val USER_DATA_KEY = "user_data"
    }

}