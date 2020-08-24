package ua.syt0r.furiganit.core.service.notification

import android.content.Context
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.app.furigana.FuriganaActivity
import ua.syt0r.furiganit.core.notification_manager.AbstractNotification
import ua.syt0r.furiganit.core.notification_manager.model.ChannelConfig
import ua.syt0r.furiganit.core.notification_manager.model.NotificationAction
import ua.syt0r.furiganit.core.notification_manager.model.NotificationConfig
import ua.syt0r.furiganit.core.service.FuriganaService

object ServiceRunningNotification : AbstractNotification {

    override fun getConfiguration(context: Context) = NotificationConfig(
        channelConfig = ChannelConfig(
            channelId = FuriganaService::class.java.name,
            channelNameResId = R.string.app_name
        ),
        smallIconResId = R.drawable.ic_notification,
        titleResId = R.string.furigana_service_running_title,
        messageResId = R.string.furigana_service_running_message,
        isOngoing = true,
        actions = listOf(
            NotificationAction(
                context.getString(R.string.furigana_service_stop),
                FuriganaService.stopServiceIntent(context)
            ),
            NotificationAction(
                context.getString(R.string.furigana_service_get_furigana),
                FuriganaActivity.startActivityIntent(context)
            )
        )
    )

}