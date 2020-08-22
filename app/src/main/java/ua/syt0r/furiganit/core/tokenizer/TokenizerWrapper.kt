package ua.syt0r.furiganit.core.tokenizer

import android.util.Log
import androidx.annotation.WorkerThread
import com.atilika.kuromoji.ipadic.Tokenizer
import ua.syt0r.furiganit.utils.isKanji
import ua.syt0r.furiganit.utils.stringToHiragana

class TokenizerWrapper {

    private var tokenizer: Tokenizer? = null

    @WorkerThread
    fun initialize() {
        if (!isInitialized()) {
            tokenizer = Tokenizer()
        }
    }

    fun isInitialized(): Boolean {
        return tokenizer != null
    }

    fun getFuriganaHtml(japaneseText: String): String {
        return tokenizer!!.getTextWithFurigana(japaneseText)
    }

    fun release() {
        Log.d(this.javaClass.simpleName, "release")
        tokenizer = null
    }

    private fun Tokenizer.getTextWithFurigana(text: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<ruby style=\"font-size:8vw;\">")

        val tokens = tokenize(text)

        for (token in tokens) {
            stringBuilder.append("<rb>").append(token.surface).append("</rb><rt>")

            if (isKanji(token.surface[0]))
                stringBuilder.append(stringToHiragana(token.reading))

            stringBuilder.append("</rt>")
        }

        stringBuilder.append("</ruby>")

        return stringBuilder.toString()
    }

}