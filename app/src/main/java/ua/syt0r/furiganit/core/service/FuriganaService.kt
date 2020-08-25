package ua.syt0r.furiganit.core.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject
import ua.syt0r.furiganit.core.notification_manager.CustomNotificationManager
import ua.syt0r.furiganit.core.service.notification.ServiceRunningNotification
import ua.syt0r.furiganit.core.service.notification.ServiceStartingNotification
import ua.syt0r.furiganit.core.tokenizer.TokenizerWrapper

class FuriganaService : Service() {

    companion object {

        private const val SERVICE_NOTIFICATION_ID = 1

        private const val STOP_SERVICE_ACTION = "stop_service"

        fun startService(context: Context) {
            val intent = Intent(context, FuriganaService::class.java)
            context.startService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, FuriganaService::class.java)
            context.stopService(intent)
        }

        fun stopServiceIntent(context: Context): PendingIntent {
            val intent = Intent(context, FuriganaService::class.java)
            intent.action = STOP_SERVICE_ACTION
            return PendingIntent.getService(context, 0, intent, 0)
        }

    }

    private val notificationManager by inject<CustomNotificationManager>()

    private val serviceStateObservable by inject<ServiceStateObservable>()

    private val tokenizerWrapper by inject<TokenizerWrapper>()

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onCreate() {
        super.onCreate()
        Log.d(this.javaClass.simpleName, "onCreate")
    }

    @ExperimentalCoroutinesApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(this.javaClass.simpleName, "onStartCommand")

        when (intent?.action) {
            STOP_SERVICE_ACTION -> stopSelf()
            else -> initializeTokenizer()
                .flowOn(Dispatchers.IO)
                .onStart {
                    val notification = notificationManager.createNotification(
                        ServiceStartingNotification.getConfiguration(this@FuriganaService)
                    )
                    startForeground(SERVICE_NOTIFICATION_ID, notification)
                    serviceStateObservable.setState(ServiceState.LAUNCHING)
                }
                .catch {
                    Toast.makeText(this@FuriganaService, "Error: " + it.message, Toast.LENGTH_LONG).show()
                    stopSelf()
                }
                .onEach {
                    serviceStateObservable.setState(ServiceState.RUNNING)
                    notificationManager.showNotification(
                        notificationId = SERVICE_NOTIFICATION_ID,
                        notificationConfig = ServiceRunningNotification.getConfiguration(this@FuriganaService)
                    )
                }
                .launchIn(serviceScope)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancelNotification(SERVICE_NOTIFICATION_ID)
        serviceStateObservable.setState(ServiceState.STOPPED)
        serviceJob.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    @ExperimentalCoroutinesApi
    private fun initializeTokenizer() = callbackFlow {
        tokenizerWrapper.initialize()
        offer(Unit)
        awaitClose {
            tokenizerWrapper.release()
        }
    }

}
