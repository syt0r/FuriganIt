package ua.syt0r.furiganit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.util.List;

public class FuriganaService extends Service {

    public static boolean isRunning = false;

    public static final String START_ACTION = "ua.syt0r.furiganit.START";
    public static final String STOP_ACTION = "ua.syt0r.furiganit.STOP";
    public static final String SERVICE_STOP = "ua.syt0r.furiganit.SERVICE_STOP";
    private static final int NOTIFICATION_ID = 1;

    private ClipboardManager clipboardManager;
    private ClipboardListener clipboardListener;

    private WindowManager windowManager;

    //Heavy object that get furigana from text
    private Tokenizer tokenizer;
    //Sends callback into activity
    private LocalBroadcastManager localBroadcastManager;


    @Override
    public void onCreate() {
        super.onCreate();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        tokenizer = new Tokenizer();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardListener = new ClipboardListener();
        clipboardManager.addPrimaryClipChangedListener(clipboardListener);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("FuriganIt")
                .setContentText("Service is running").setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent stopServiceIntent = new Intent(this, FuriganaService.class);
        stopServiceIntent.setAction(SERVICE_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.mipmap.ic_launcher_round,"Stop",pendingIntent);

        Notification notification = builder.build();
        startForeground(NOTIFICATION_ID,notification);

        Intent intent = new Intent();
        intent.setAction(START_ACTION);
        localBroadcastManager.sendBroadcast(intent);

        isRunning = true;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction() != null && intent.getAction().equals(SERVICE_STOP)){
            stopSelf();
            Log.wtf("test","stop");
        }

        return super.onStartCommand(intent, flags, startId);
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

        tokenizer = null;

        if (clipboardManager!=null)
            clipboardManager.removePrimaryClipChangedListener(clipboardListener);

        Intent intent = new Intent();
        intent.setAction(STOP_ACTION);
        localBroadcastManager.sendBroadcast(intent);

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

            //final WebView view = new WebView(FuriganaService.this);

            ClipData clip = clipboardManager.getPrimaryClip();
            CharSequence data = clip.getItemAt(0).getText();
            List<Token> tokens;
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ruby>");
            if (data != null){
                tokens = tokenizer.tokenize(data.toString());
                for (Token token : tokens){
                    stringBuilder.append("<rb>").append(token.getSurface()).append("</rb><rt>");
                    if (Utils.isKanji(token.getSurface().charAt(0)))
                        stringBuilder.append(Utils.stringToHiragana(token.getReading()));
                    stringBuilder.append("</rt>");
                }
            }
            else
                stringBuilder.append("Not supported data copied");
            stringBuilder.append("</ruby>");

            Log.wtf("test",stringBuilder.toString());

            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }

            //Show data
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
                    PixelFormat.TRANSLUCENT);

            windowManager.addView(view, params);

            //Button on touch timeout
            CountDownTimer countDownTimer = new CountDownTimer(5*1000,1000) {
                @Override
                public void onTick(long l) {}

                @Override
                public void onFinish() {
                    synchronized (view){
                        if(view.getWindowToken() != null)
                            windowManager.removeView(view);
                    }
                }
            };
            countDownTimer.start();

            GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(FuriganaService.this, new SingleTapConfirm());
            ((Button)view.findViewById(R.id.button)).setOnTouchListener(new CustomTouchListener(view,params,stringBuilder,gestureDetectorCompat, countDownTimer));

        }
    }

    private class CustomTouchListener implements View.OnTouchListener{

        private final View view;
        private WindowManager.LayoutParams params;
        private StringBuilder stringBuilder;
        private GestureDetectorCompat gestureDetectorCompat;
        private CountDownTimer countDownTimer;

        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;

        CustomTouchListener(View view, WindowManager.LayoutParams params, StringBuilder stringBuilder, GestureDetectorCompat gestureDetectorCompat, CountDownTimer countDownTimer){
            this.view = view;
            this.params = params;
            this.stringBuilder = stringBuilder;
            this.gestureDetectorCompat = gestureDetectorCompat;
           this.countDownTimer = countDownTimer;
        }

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            //If clicked
            if (gestureDetectorCompat.onTouchEvent(motionEvent)){
                Intent intent = new Intent(FuriganaService.this, FuriganizedActivity.class);
                intent.putExtra("data",stringBuilder.toString());
                startActivity(intent);
                synchronized (view){
                    if(view.getWindowToken() != null)
                        windowManager.removeView(view);
                }
                return true;
            }

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
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

}
