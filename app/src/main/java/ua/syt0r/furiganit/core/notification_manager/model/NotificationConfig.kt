package ua.syt0r.furiganit.core.notification_manager.model

import android.app.PendingIntent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NotificationConfig(
    val channelConfig: ChannelConfig,
    @DrawableRes val smallIconResId: Int,
    @StringRes val titleResId: Int,
    @StringRes val messageResId: Int,
    val isOngoing: Boolean,
    val clickIntent: PendingIntent? = null,
    val actions: List<NotificationAction> = emptyList()
)