package ua.syt0r.furiganit.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.app.service.FuriganaService.Companion.SERVICE_STOP

class ServiceNotificationManager(context: Context) {

    private val builder: NotificationCompat.Builder

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(context)

        builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_ik)
                .setContentTitle(context.resources.getString(R.string.app_name))
                .setContentText(context.resources.getString(R.string.service_is_running))
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val stopServiceIntent = Intent(context, FuriganaService::class.java)
        stopServiceIntent.action = SERVICE_STOP
        val pendingIntent = PendingIntent.getService(context, 0,
                stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.addAction(R.drawable.ic_stat_ik, context.resources.getString(R.string.stop), pendingIntent)

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun buildServiceNotification(): Notification {
        return builder.build()
    }

    companion object {
        private const val CHANNEL_ID = "ua.syt0r.furigan_it"
        private const val CHANNEL_NAME = "FuriganIt Service"
    }

}
