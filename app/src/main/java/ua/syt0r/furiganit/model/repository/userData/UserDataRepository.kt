package ua.syt0r.furiganit.model.repository.userData

import io.reactivex.Completable
import io.reactivex.Single
import ua.syt0r.furiganit.model.entity.UserData

interface UserDataRepository {
    fun saveUserData(userData: UserData): Completable
    fun getUserData(): Single<UserData>
    fun clearUserData(): Completable
}
