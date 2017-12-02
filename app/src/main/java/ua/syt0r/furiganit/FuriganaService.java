package ua.syt0r.furiganit;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FuriganaService extends Service {

    public static boolean isRunning = false;

    private ClipboardManager clipboardManager;

    private ClipboardManager.OnPrimaryClipChangedListener primaryClipChangedListener;

    @Override
    public void onCreate() {
        super.onCreate();

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        primaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData clip = clipboardManager.getPrimaryClip();
                Log.wtf("test",""+clip.getItemAt(0).getText());
            }

        };

        clipboardManager.addPrimaryClipChangedListener(primaryClipChangedListener);

        isRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (clipboardManager!=null)
            clipboardManager.removePrimaryClipChangedListener(primaryClipChangedListener);

        isRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
