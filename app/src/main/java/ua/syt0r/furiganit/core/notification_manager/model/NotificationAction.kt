package ua.syt0r.furiganit.core.notification_manager.model

import android.app.PendingIntent

data class NotificationAction(
        val message: String,
        val intent: PendingIntent
)