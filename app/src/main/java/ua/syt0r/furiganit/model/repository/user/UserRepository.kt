package ua.syt0r.furiganit.model.repository.user

import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.UserData

interface UserRepository {
    fun saveUserData(userData: UserData): Completable
    fun getUserData(): Single<UserData>
    fun clearUserData(): Completable
}
