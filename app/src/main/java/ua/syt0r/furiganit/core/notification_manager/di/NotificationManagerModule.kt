package ua.syt0r.furiganit.core.notification_manager.di

import android.app.NotificationManager
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.syt0r.furiganit.core.notification_manager.CustomNotificationManager

val notificationManagerModule = module {

    single { androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    single { CustomNotificationManager(androidContext(), get()) }

}