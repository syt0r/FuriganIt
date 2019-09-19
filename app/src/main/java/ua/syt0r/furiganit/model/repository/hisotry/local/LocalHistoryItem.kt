package ua.syt0r.furiganit.model.repository.hisotry.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_items")
data class LocalHistoryItem(
    @PrimaryKey var textHash: String = "",
    @ColumnInfo var text: String = "",
    @ColumnInfo var textWithFurigana: String = ""
)