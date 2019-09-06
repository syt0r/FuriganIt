package ua.syt0r.furiganit.app.furigana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import ua.syt0r.furiganit.R

class FuriganaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_furigana)
        overridePendingTransition(R.anim.activity_start, R.anim.activity_finish)

        val text = intent.getStringExtra(TEXT_EXTRA_FIELD)
        val furigana = intent.getStringExtra(FURIGANA_EXTRA_FIELD)

        val error = intent.getStringExtra(ERROR_EXTRA_FIELD)

        val webView = findViewById<WebView>(R.id.webview)
        webView.loadData(furigana, "text/html; charset=utf-8", "utf-8")
    }

    fun Click(v: View) {
        overridePendingTransition(R.anim.activity_start, R.anim.activity_finish)
        finish()
    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.activity_start, R.anim.activity_finish)
        super.onBackPressed()
    }

    companion object {

        private const val TEXT_EXTRA_FIELD = "text"
        private const val FURIGANA_EXTRA_FIELD = "furigana"
        private const val ERROR_EXTRA_FIELD = "error"

        fun getArgs(text: String, furigana: String) = Bundle().apply {
            putString(TEXT_EXTRA_FIELD, text)
            putString(FURIGANA_EXTRA_FIELD, furigana)
        }

        fun getArgs(error: String) = Bundle().apply {
            putString(ERROR_EXTRA_FIELD, error)
        }

    }
}
