package ua.syt0r.furiganit.core.clipboard

import android.content.ClipboardManager

class ClipboardHandler(
        private val clipboardManager: ClipboardManager
) {

    private val clipChangeListener = C()

    private var onChange: (() -> Unit)? = null

    fun setListener(listener: (() -> Unit)?) {
        onChange = listener
        clipboardManager.addPrimaryClipChangedListener(clipChangeListener)
    }

    fun getClipboardText(): String? {
        return clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()
    }

    inner class C : ClipboardManager.OnPrimaryClipChangedListener {

        override fun onPrimaryClipChanged() {
            onChange?.invoke()
        }

    }

}