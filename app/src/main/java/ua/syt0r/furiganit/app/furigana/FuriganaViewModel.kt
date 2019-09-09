package ua.syt0r.furiganit.app.furigana

import androidx.lifecycle.ViewModel
import ua.syt0r.furiganit.model.db.HistoryDatabase

class FuriganaViewModel(
        database: HistoryDatabase
) : ViewModel() {

    fun saveItemToHistory(text: String, furigana: String) {



    }

    override fun onCleared() {
        super.onCleared()

    }

}