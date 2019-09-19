package ua.syt0r.furiganit.model.usecase.implementation

import android.content.Context
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.model.usecase.TextLocalizer

class TextLocalizerImpl(private val context: Context): TextLocalizer {

    override fun getMessage(messageId: Int) = context.getString(messageId)

    override fun getErrorMessage(error: String?): String {
        return "${context.getString(R.string.error)}: $error"
    }

}