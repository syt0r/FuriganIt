package ua.syt0r.furiganit.model.repository.user

import android.service.autofill.UserData
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun saveUserData(userData: UserData): Completable
    fun getUserData(): Single<UserData>
    fun clearUserData(): Completable
}
