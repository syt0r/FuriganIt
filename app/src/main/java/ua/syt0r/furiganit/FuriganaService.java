package ua.syt0r.furiganit;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class FuriganaService extends Service {

    public static boolean isRunning = false;

    private ClipboardManager clipboardManager;
    private ClipboardListener clipboardListener;

    private WindowManager windowManager;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardListener = new ClipboardListener();
        clipboardManager.addPrimaryClipChangedListener(clipboardListener);

        isRunning = true;
    }

    void startPrimaryClipChangedListenerDelayThread() {
        clipboardManager.removePrimaryClipChangedListener(clipboardListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clipboardManager.addPrimaryClipChangedListener(clipboardListener);
            }
        }, 500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (clipboardManager!=null)
            clipboardManager.removePrimaryClipChangedListener(clipboardListener);

        isRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener{

        @Override
        public void onPrimaryClipChanged() {
            //add delay before next check(browser bug - copying twice)
            startPrimaryClipChangedListenerDelayThread();

            //Show button for furiginize text
            final View view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_ask,null);

            int overlayParam;
            if (Build.VERSION.SDK_INT < 26)
                overlayParam = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            else
                overlayParam = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    overlayParam,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.START | Gravity.TOP;

            windowManager.addView(view, params);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    windowManager.removeView(view);
                }
            },5*1000);


            ClipData clip = clipboardManager.getPrimaryClip();
            Log.wtf("test",""+clip.getItemAt(0).getText());
        }
    }

}
