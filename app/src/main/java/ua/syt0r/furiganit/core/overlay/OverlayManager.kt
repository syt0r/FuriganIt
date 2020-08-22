package ua.syt0r.furiganit.core.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.LayoutRes
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class OverlayManager(
        private val context: Context,
        private val windowManager: WindowManager,
        private val overlayDataRepository: OverlayDataRepository
) {

    companion object {
        private val AUTO_CLOSE_DELAY = TimeUnit.SECONDS.toMillis(2)
    }

    private val touchListener = OverlayTouchListener(context, windowManager)

    var onOverlayClickListener: () -> Unit = {}

    fun canDrawOverlay(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Settings.canDrawOverlays(context)
        else true
    }

    fun showOverlay(@LayoutRes layoutResId: Int) {
        val overlayView = LayoutInflater.from(context).inflate(layoutResId, null)
        val layoutParams = setupLayoutParams()

        val viewReference = WeakReference(overlayView)
        val autoCloseRunnable = Runnable {
            viewReference.get()?.also { windowManager.removeView(it) }
        }

        overlayView.setOnTouchListener(touchListener)
        overlayView.setOnClickListener {
            overlayView.removeCallbacks(autoCloseRunnable)
            onOverlayClickListener.invoke()
        }

        windowManager.addView(overlayView, layoutParams)
        overlayView.postDelayed(autoCloseRunnable, AUTO_CLOSE_DELAY)
    }

    private fun setupLayoutParams(): WindowManager.LayoutParams {
        val layoutFlags: Int = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else -> WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlags,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        val position = overlayDataRepository.position

        params.x = position.x
        params.y = position.y

        return params
    }

}
