package ua.syt0r.furiganit.service;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.core.view.GestureDetectorCompat;

class OverlayTouchListener implements View.OnTouchListener {

    private WindowManager windowManager;

    private GestureDetectorCompat gestureDetectorCompat;
    private CountDownTimer countDownTimer;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    public OverlayTouchListener(Context context, WindowManager windowManager) {
        this.windowManager = windowManager;
        gestureDetectorCompat = new GestureDetectorCompat(context, new SingleTapConfirm());
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        //If clicked
        if (gestureDetectorCompat.onTouchEvent(motionEvent)) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
            view.performClick();
            return true;
        }

        WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();

        //if dragged
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = params.x;
                initialY = params.y;
                initialTouchX = motionEvent.getRawX();
                initialTouchY = motionEvent.getRawY();
                resetTimer();
                return true;
            case MotionEvent.ACTION_UP:
                resetTimer();
                return true;
            case MotionEvent.ACTION_MOVE:
                params.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                params.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                windowManager.updateViewLayout(view, params);
                resetTimer();
                return true;
        }

        return false;
    }

    private void resetTimer(){
        countDownTimer.cancel();
        countDownTimer.start();
    }

    static class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

}