package ua.syt0r.furiganit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

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

    //Position of floating window
    private int xPos, yPos;

    private long timeout;

    @Override
    public void onCreate() {
        super.onCreate();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardListener = new ClipboardListener();
        clipboardManager.addPrimaryClipChangedListener(clipboardListener);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"furigan_it_channel_id")
                .setSmallIcon(R.drawable.ic_stat_ik)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.service_is_running)).setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent stopServiceIntent = new Intent(this, FuriganaService.class);
        stopServiceIntent.setAction(SERVICE_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_stat_ik, getResources().getString(R.string.stop), pendingIntent);


        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                xPos = sharedPreferences.getInt("xPos",0);
                yPos = sharedPreferences.getInt("yPos",0);
                timeout = sharedPreferences.getLong("timeout",5);
                try {
                    tokenizer = new Tokenizer();
                }catch (OutOfMemoryError e){
                    Toast.makeText(getApplicationContext(),R.string.no_memory,Toast.LENGTH_LONG).show();
                    stopSelf();
                }

                startForeground(NOTIFICATION_ID, builder.build());

                isRunning = true;

                Intent intent = new Intent();
                intent.setAction(START_ACTION);
                localBroadcastManager.sendBroadcast(intent);

            }
        }).start();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getAction() != null && intent.getAction().equals(SERVICE_STOP)){
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

        //Save data
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("xPos",xPos).putInt("yPos",yPos).apply();

        isRunning = false;

        Intent intent = new Intent();
        intent.setAction(STOP_ACTION);
        localBroadcastManager.sendBroadcast(intent);

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

            ClipData clip = clipboardManager.getPrimaryClip();
            CharSequence data = clip.getItemAt(0).getText();
            List<Token> tokens;
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ruby style=\"font-size:8vw;\">");
            if (data != null){
                tokens = tokenizer.tokenize(String.valueOf(data));
                for (Token token : tokens){
                    stringBuilder.append("<rb>").append(token.getSurface()).append("</rb><rt>");
                    if (Utils.isKanji(token.getSurface().charAt(0)))
                        stringBuilder.append(Utils.stringToHiragana(token.getReading()));
                    stringBuilder.append("</rt>");
                }
            }
            else
                stringBuilder.append(getResources().getString(R.string.copied_data_not_supported));
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

            params.x = xPos;
            params.y = yPos;

            windowManager.addView(view, params);

            //Button on touch timeout
            CountDownTimer countDownTimer = new CountDownTimer(timeout*1000,1000) {
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
            (view.findViewById(R.id.button)).setOnTouchListener(new CustomTouchListener(view,params,stringBuilder,gestureDetectorCompat, countDownTimer));

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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    xPos = params.x;
                    yPos = params.y;
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
