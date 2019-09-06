package ua.syt0r.furiganit.app.service

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.model.repository.overlay.OverlayDataRepository

class OverlayDisplayManager(context: Context, onOverlayClickListener: View.OnClickListener) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val overlayDataRepository: OverlayDataRepository = OverlayDataRepository(context)

    private val touchListener = OverlayTouchListener(context, windowManager)

    private val overlayView = LayoutInflater.from(context).inflate(R.layout.overlay, null).apply {
        setOnTouchListener(touchListener)
        setOnClickListener(onOverlayClickListener)
    }

    fun showView() {

        val layoutFlags: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlags = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutFlags = WindowManager.LayoutParams.TYPE_PHONE
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

        //Button on touch timeout
        val countDownTimer = object : CountDownTimer(overlayDataRepository.timeout * 1000, 1000) {

            override fun onTick(l: Long) {}

            override fun onFinish() {
                overlayView.post {
                    if (overlayView != null)
                        windowManager.removeView(overlayView)
                }
            }

        }

        touchListener.setCountDownTimer(countDownTimer)
        countDownTimer.start()

        windowManager.addView(overlayView, params)

    }

}
