package ua.syt0r.furiganit.core.notification_manager.notification

import android.content.Context
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.app.furigana.FuriganaActivity
import ua.syt0r.furiganit.core.service.FuriganaService
import ua.syt0r.furiganit.core.notification_manager.AbstractNotification
import ua.syt0r.furiganit.core.notification_manager.model.ChannelConfig
import ua.syt0r.furiganit.core.notification_manager.model.NotificationAction
import ua.syt0r.furiganit.core.notification_manager.model.NotificationConfig
import java.util.*

object ServiceNotification : AbstractNotification {

    override fun getConfiguration(context: Context) = NotificationConfig(
            channelConfig = ChannelConfig(
                    channelId = UUID.randomUUID().toString(),
                    channelNameResId = R.string.app_name
            ),
            smallIconResId = R.drawable.ic_notification,
            titleResId = R.string.app_name,
            messageResId = R.string.app_name,
            isOngoing = true,
            actions = listOf(
                    NotificationAction(
                            "Stop Service",
                            FuriganaService.stopServiceIntent(context)
                    ),
                    NotificationAction(
                            "Open Activity",
                            FuriganaActivity.startActivityIntent(context)
                    )
            )
    )

}