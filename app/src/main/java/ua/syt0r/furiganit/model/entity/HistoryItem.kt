package ua.syt0r.furiganit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class HistoryItem(
    @PrimaryKey var uuid: String = "",
    @ColumnInfo var text: String = "",
    @ColumnInfo var textWithFurigana: String = "",
    @Ignore var documentId: String? = null,
    @Ignore var user: String? = null
)
