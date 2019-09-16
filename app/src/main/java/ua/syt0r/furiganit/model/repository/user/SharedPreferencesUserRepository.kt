package ua.syt0r.furiganit.model.repository.user

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.UserData

class SharedPreferencesUserRepository(context: Context) : UserRepository {

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun saveUserData(userData: UserData) = Completable.create { emitter ->
        sharedPreferences.edit().putString("user_data", Gson().toJson(userData))
        emitter.onComplete()
    }

    override fun getUserData(): Single<UserData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearUserData(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val PREF_NAME = "user_data"
    }

}