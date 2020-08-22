package ua.syt0r.furiganit.core.clipboard.di

import android.content.ClipboardManager
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.syt0r.furiganit.core.clipboard.ClipboardHandler

val clipboardHandlerModule = module {

    single { androidContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    single { ClipboardHandler(get()) }

}