package ua.syt0r.furiganit.model.repository.hisotry.remote

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class RemoteHistoryItem(
    @PrimaryKey var textHash: String = "",
    @ColumnInfo var user: String = "",
    @ColumnInfo var synced: Boolean = false
)