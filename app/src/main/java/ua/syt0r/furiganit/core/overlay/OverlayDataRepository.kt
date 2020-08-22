package ua.syt0r.furiganit.core.overlay

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import org.jetbrains.annotations.TestOnly

class OverlayDataRepository {

    companion object {
        private const val PREF_NAME = "overlay_window_pos"
        private const val X_POS = "xPos"
        private const val Y_POS = "yPos"
        private const val TIMEOUT = "timeout"
    }

    private val sharedPreferences: SharedPreferences

    constructor(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @TestOnly
    constructor(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    var position: Point
        get() {
            val x = sharedPreferences.getInt(X_POS, 0)
            val y = sharedPreferences.getInt(Y_POS, 0)
            return Point(x, y)
        }
        set(value) = sharedPreferences.edit()
                .putInt(X_POS, value.x)
                .putInt(Y_POS, value.y)
                .apply()

    var timeout: Long
        get() = sharedPreferences.getLong(TIMEOUT, 5)
        set(value) = sharedPreferences.edit().putLong(TIMEOUT, value).apply()

}
