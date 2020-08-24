package ua.syt0r.furiganit.app.furigana

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_furigana.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.R

class FuriganaActivity : AppCompatActivity() {

    companion object {
        private const val MIME_TYPE = "text/html; charset=utf-8"
        private const val ENCODING = "utf-8"

        fun startActivityIntent(context: Context): PendingIntent {
            return PendingIntent.getActivity(
                context,
                0,
                Intent(context, FuriganaActivity::class.java),
                0
            )
        }

    }

    private val viewModel: FuriganaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_furigana)

        viewModel.subscribeOnState().observe(this, Observer { state ->
            when (state) {
                is FuriganaViewModel.State.Loading -> {

                }
                is FuriganaViewModel.State.Loaded -> {
                    webView.loadData(state.html, MIME_TYPE, ENCODING)
                }
                is FuriganaViewModel.State.Error -> {

                }
            }
        })

        closeButton.setOnClickListener { finish() }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus)
            viewModel.loadData(intent)
    }

    override fun onStop() {
        overridePendingTransition(R.anim.activity_start, R.anim.activity_finish)
        super.onStop()
    }

}
