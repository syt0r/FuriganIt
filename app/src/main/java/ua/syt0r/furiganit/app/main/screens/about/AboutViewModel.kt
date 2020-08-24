package ua.syt0r.furiganit.app.main.screens.about

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {

    companion object {
        private const val SOURCE_CODE_URL = "https://github.com/SYtor/FuriganIt"
    }

    fun openSourceCodeWebPage(activity: Activity) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(SOURCE_CODE_URL)
        )
        activity.startActivity(browserIntent)
    }

}