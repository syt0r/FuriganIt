package ua.syt0r.furiganit.app.furigana

import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ua.syt0r.furiganit.core.clipboard.ClipboardHandler
import ua.syt0r.furiganit.core.tokenizer.TokenizerWrapper

class FuriganaViewModel(
        private val clipboardHandler: ClipboardHandler,
        private val tokenizerWrapper: TokenizerWrapper
) : ViewModel() {

    enum class ErrorReason {
        TOKENIZER_INIT_FAILURE,
        CANT_GET_TEXT
    }

    sealed class State {
        object Loading : State()
        class Loaded(val html: String) : State()
        class Error(val reason: ErrorReason) : State()
    }

    private val state = MutableLiveData<State>(State.Loading)

    fun loadData(intent: Intent) {
        val japaneseText = getJapaneseText(intent)

        if (tokenizerWrapper.isInitialized()) {
            applyFinalState(japaneseText)
        } else {
            initializeTokenizer()
                    .flowOn(Dispatchers.IO)
                    .catch { state.value = State.Error(ErrorReason.TOKENIZER_INIT_FAILURE) }
                    .onEach { applyFinalState(japaneseText) }
                    .launchIn(viewModelScope)
        }
    }

    fun subscribeOnState(): LiveData<State> = state

    private fun getJapaneseText(intent: Intent): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
                    ?: clipboardHandler.getClipboardText()
        } else {
            clipboardHandler.getClipboardText()
        }
    }

    private fun applyFinalState(japaneseText: String?) {
        state.value = when {
            japaneseText.isNullOrEmpty() -> State.Error(ErrorReason.CANT_GET_TEXT)
            else -> {
                val furigana = tokenizerWrapper.getFuriganaHtml(japaneseText)
                State.Loaded(furigana)
            }
        }
    }

    private fun initializeTokenizer() = flow {
        tokenizerWrapper.initialize()
        emit(Unit)
    }

}