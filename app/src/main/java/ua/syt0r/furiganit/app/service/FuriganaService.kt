package ua.syt0r.furiganit.app.service

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.atilika.kuromoji.ipadic.Tokenizer
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.model.repository.status.ServiceStatus
import ua.syt0r.furiganit.model.repository.status.ServiceStatusRepository
import ua.syt0r.furiganit.app.furigana.FuriganaActivity
import ua.syt0r.furiganit.utils.getTextWithFurigana

class FuriganaService : Service() {

    private lateinit var notificationManager: ServiceNotificationManager
    private lateinit var serviceStatusRepository: ServiceStatusRepository

    private lateinit var overlayDisplayManager: OverlayDisplayManager

    private lateinit var clipboardManager: ClipboardManager
    private lateinit var clipboardListener: ClipboardManager.OnPrimaryClipChangedListener

    private var tokenizer: Tokenizer? = null

    override fun onCreate() {
        super.onCreate()

        notificationManager = ServiceNotificationManager(this)
        serviceStatusRepository = ServiceStatusRepository(this)

        serviceStatusRepository.setStatus(ServiceStatus.LAUNCHING)

        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        overlayDisplayManager = OverlayDisplayManager(this, View.OnClickListener {

            val intent = Intent(this@FuriganaService, FuriganaActivity::class.java)

            val clip = clipboardManager.primaryClip

            try {

                val data = clip!!.getItemAt(0).text
                val text = data.toString()
                val furigana = getTextWithFurigana(tokenizer!!, text)
                intent.putExtras(FuriganaActivity.getArgs(text, furigana))

            } catch (e: Exception) {
                e.printStackTrace()
                intent.putExtras(FuriganaActivity.getArgs(e.toString()))
            }

            startActivity(intent)

        })

        clipboardListener = ClipboardManager.OnPrimaryClipChangedListener {

            //Bypass browsers bug that copies text twice.
            clipboardManager.removePrimaryClipChangedListener(clipboardListener)
            Handler().postDelayed({ clipboardManager.addPrimaryClipChangedListener(clipboardListener) }, 500)

            overlayDisplayManager.showView()
        }

        Thread {

            try {

                tokenizer = Tokenizer()
                startForeground(NOTIFICATION_ID, notificationManager.buildServiceNotification())
                serviceStatusRepository.setStatus(ServiceStatus.RUNNING)

                clipboardManager.addPrimaryClipChangedListener(clipboardListener)

            } catch (e: OutOfMemoryError) {

                Toast.makeText(applicationContext, R.string.no_memory, Toast.LENGTH_LONG).show()
                stopSelf()

            }

        }.start()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null && intent.action != null && intent.action == SERVICE_STOP) {
            stopSelf()
            Log.wtf("test", "stop")
        }

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()

        tokenizer = null

        clipboardManager.removePrimaryClipChangedListener(clipboardListener)
        serviceStatusRepository.setStatus(ServiceStatus.STOPPED)

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {

        const val SERVICE_STOP = "ua.syt0r.furiganit.SERVICE_STOP"
        private const val NOTIFICATION_ID = 1
    }

}
