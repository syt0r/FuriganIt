package ua.syt0r.furiganit.core.overlay

import android.content.Context
import android.os.CountDownTimer
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.view.GestureDetectorCompat

class OverlayTouchListener(
        context: Context,
        private val windowManager: WindowManager
) : View.OnTouchListener {

    private val gestureDetectorCompat = GestureDetectorCompat(context, SingleTapConfirm())
    private var countDownTimer: CountDownTimer? = null

    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0f
    private var initialTouchY: Float = 0f

    fun setCountDownTimer(countDownTimer: CountDownTimer) {
        this.countDownTimer = countDownTimer
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        //If clicked
        if (gestureDetectorCompat.onTouchEvent(motionEvent)) {
            countDownTimer?.cancel()
            countDownTimer?.onFinish()
            view.performClick()
            return true
        }

        val params = view.layoutParams as WindowManager.LayoutParams

        //if dragged
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = params.x
                initialY = params.y
                initialTouchX = motionEvent.rawX
                initialTouchY = motionEvent.rawY
                resetTimer()
                return true
            }
            MotionEvent.ACTION_UP -> {
                resetTimer()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                params.x = initialX + (motionEvent.rawX - initialTouchX).toInt()
                params.y = initialY + (motionEvent.rawY - initialTouchY).toInt()
                windowManager.updateViewLayout(view, params)
                resetTimer()
                return true
            }
        }

        return false
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        countDownTimer?.start()
    }

    class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return true
        }
    }

}