package ua.syt0r.furiganit.model.usecase.implementation

import android.content.Context
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.model.usecase.TextLocalizerUseCase

class TextLocalizerUseCaseImpl(private val context: Context): TextLocalizerUseCase {

    override fun getMessage(messageId: Int) = context.getString(messageId)

    override fun getErrorMessage(error: String?): String {
        return "${context.getString(R.string.error)}: $error"
    }

}