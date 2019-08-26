package ua.syt0r.furiganit.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class HistoryItem {

    @PrimaryKey @NonNull private String uuid = "";
    @ColumnInfo private String text;
    @ColumnInfo private String textWithFurigana;
    @Ignore private String documentId;
    @Ignore private String user;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextWithFurigana() {
        return textWithFurigana;
    }

    public void setTextWithFurigana(String textWithFurigana) {
        this.textWithFurigana = textWithFurigana;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
