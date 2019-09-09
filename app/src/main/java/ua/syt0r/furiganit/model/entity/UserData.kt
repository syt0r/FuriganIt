package ua.syt0r.furiganit.model.entity

import com.google.firebase.firestore.DocumentReference

data class UserData(
        val user: String = "",
        val update_time: Long = 0L,
        val history: List<DocumentReference> = ArrayList()
)