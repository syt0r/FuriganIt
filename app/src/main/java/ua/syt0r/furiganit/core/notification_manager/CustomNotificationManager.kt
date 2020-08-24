package ua.syt0r.furiganit.core.notification_manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ua.syt0r.furiganit.core.notification_manager.model.ChannelConfig
import ua.syt0r.furiganit.core.notification_manager.model.NotificationAction
import ua.syt0r.furiganit.core.notification_manager.model.NotificationConfig

class CustomNotificationManager(
    private val context: Context,
    private val notificationManager: NotificationManager
) {

    fun createNotification(notificationConfig: NotificationConfig): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationConfig.channelConfig)

        val builder = notificationConfig.run {
            NotificationCompat.Builder(context, channelConfig.channelId)
                .setSmallIcon(smallIconResId)
                .setContentTitle(context.resources.getString(titleResId))
                .setContentText(context.resources.getString(messageResId))
                .setOngoing(isOngoing)
                .also { builder -> clickIntent?.let { builder.setContentIntent(it) } }
                .also { builder ->
                    actions.forEach { builder.addAction(it.toAndroidNotificationAction()) }
                }
        }

        return builder.build()
    }

    fun showNotification(notificationId: Int, notificationConfig: NotificationConfig) {
        notificationManager.notify(notificationId, createNotification(notificationConfig))
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelConfig: ChannelConfig) {
        val channel = NotificationChannel(
            channelConfig.channelId,
            context.resources.getString(channelConfig.channelNameResId),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        if (channelConfig.disableSound) {
            channel.setSound(null, null)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun NotificationAction.toAndroidNotificationAction() =
        NotificationCompat.Action.Builder(0, message, intent).build()

}
