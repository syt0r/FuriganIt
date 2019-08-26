package ua.syt0r.furiganit.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryItem {

    @PrimaryKey private String uuid;
    @ColumnInfo private String text;
    @ColumnInfo private String textWithFurigana;

}
