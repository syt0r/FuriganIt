package ua.syt0r.furiganit.core.notification_manager.model

import androidx.annotation.StringRes

data class ChannelConfig(
        @StringRes val channelNameResId: Int,
        val channelId: String
)