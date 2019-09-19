package ua.syt0r.furiganit.model.repository.hisotry.local.implementation

import ua.syt0r.furiganit.model.entity.HistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryItem
import ua.syt0r.furiganit.model.repository.hisotry.local.LocalHistoryItemMapper
import java.math.BigInteger
import java.security.MessageDigest

class LocalHistoryItemMapperImpl : LocalHistoryItemMapper {

    override fun toLocalHistoryItem(historyItem: HistoryItem): LocalHistoryItem {
        return LocalHistoryItem(
            textToHash(historyItem.text),
            historyItem.text,
            historyItem.textWithFurigana
        )
    }

    override fun toHistoryItem(localHistoryItem: LocalHistoryItem): HistoryItem {
        return HistoryItem(localHistoryItem.text, localHistoryItem.textWithFurigana)
    }

    private fun textToHash(text: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(text.toByteArray()))
            .toString(16).padStart(32, '0')
    }

}