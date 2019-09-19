package ua.syt0r.furiganit.model.usecase

interface TextLocalizer {
    fun getMessage(messageId: Int): String
    fun getErrorMessage(error: String?): String
}