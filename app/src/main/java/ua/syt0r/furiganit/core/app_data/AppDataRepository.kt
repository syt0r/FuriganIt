package ua.syt0r.furiganit.core.app_data

import android.content.Context

class AppDataRepository(context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "app_data"

        private const val FURIGANIZED_TEXTS_COUNT_KEY = "furiganized_texts"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    var furiganizedTextsCountSinceReview: Long
        set(value) {
            sharedPreferences.edit().putLong(FURIGANIZED_TEXTS_COUNT_KEY, value).apply()
        }
        get() = sharedPreferences.getLong(FURIGANIZED_TEXTS_COUNT_KEY, 0)

}