package ua.syt0r.furiganit.core.service.notification

import android.content.Context
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.core.notification_manager.AbstractNotification
import ua.syt0r.furiganit.core.notification_manager.model.ChannelConfig
import ua.syt0r.furiganit.core.notification_manager.model.NotificationConfig
import ua.syt0r.furiganit.core.service.FuriganaService

object ServiceStartingNotification : AbstractNotification {

    override fun getConfiguration(context: Context) = NotificationConfig(
        channelConfig = ChannelConfig(
            channelId = FuriganaService::class.java.name,
            channelNameResId = R.string.app_name
        ),
        smallIconResId = R.drawable.ic_notification,
        titleResId = R.string.furigana_service_starting_title,
        messageResId = R.string.furigana_service_starting_message,
        isOngoing = true
    )

}