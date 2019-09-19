package ua.syt0r.furiganit.model.repository.hisotry.local

import ua.syt0r.furiganit.model.entity.HistoryItem

interface LocalHistoryItemMapper {
    fun toLocalHistoryItem(historyItem: HistoryItem): LocalHistoryItem
    fun toHistoryItem(localHistoryItem: LocalHistoryItem): HistoryItem
}