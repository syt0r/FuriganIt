package ua.syt0r.furiganit.service;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.core.view.GestureDetectorCompat;

import ua.syt0r.furiganit.R;
import ua.syt0r.furiganit.model.repository.overlay.OverlayDataRepository;

public class OverlayDisplayManager {

    private WindowManager windowManager;
    private OverlayDataRepository overlayDataRepository;

    private final View overlayView;
    private OverlayTouchListener touchListener;

    public OverlayDisplayManager(Context context, View.OnClickListener onOverlayClickListener) {

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        overlayDataRepository = new OverlayDataRepository(context);

        overlayView = LayoutInflater.from(context).inflate(R.layout.overlay,null);

        touchListener = new OverlayTouchListener(context, windowManager);
        overlayView.setOnTouchListener(touchListener);

        overlayView.setOnClickListener(onOverlayClickListener);

    }

    public void showView() {

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
                PixelFormat.TRANSLUCENT);

        Point position = overlayDataRepository.getPosition();

        params.x = position.x;
        params.y = position.y;

        //Button on touch timeout
        CountDownTimer countDownTimer = new CountDownTimer(overlayDataRepository.getTimeout() * 1000,1000) {

            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                synchronized (overlayView){
                    if(overlayView.getWindowToken() != null)
                        windowManager.removeView(overlayView);
                }
            }

        };

        touchListener.setCountDownTimer(countDownTimer);
        countDownTimer.start();

        windowManager.addView(overlayView, params);

    }

}
