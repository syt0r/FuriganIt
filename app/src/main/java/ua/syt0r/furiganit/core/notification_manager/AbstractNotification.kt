package ua.syt0r.furiganit.core.notification_manager

import android.content.Context
import ua.syt0r.furiganit.core.notification_manager.model.NotificationConfig

interface AbstractNotification {
    fun getConfiguration(context: Context): NotificationConfig
}