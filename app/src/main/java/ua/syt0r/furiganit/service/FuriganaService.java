package ua.syt0r.furiganit.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.atilika.kuromoji.ipadic.Tokenizer;
import ua.syt0r.furiganit.R;
import ua.syt0r.furiganit.model.repository.status.ServiceStatus;
import ua.syt0r.furiganit.model.repository.status.ServiceStatusRepository;
import ua.syt0r.furiganit.ui.furigana.FuriganaActivity;

public class FuriganaService extends Service {

    public static final String SERVICE_STOP = "ua.syt0r.furiganit.SERVICE_STOP";
    private static final int NOTIFICATION_ID = 1;

    private ServiceNotificationManager notificationManager;
    private ServiceStatusRepository serviceStatusRepository;

    private OverlayDisplayManager overlayDisplayManager;

    private ClipboardManager clipboardManager;
    private ClipboardManager.OnPrimaryClipChangedListener clipboardListener;

    private Tokenizer tokenizer;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = new ServiceNotificationManager(this);
        serviceStatusRepository = new ServiceStatusRepository(this);

        serviceStatusRepository.setStatus(ServiceStatus.LAUNCHING);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        overlayDisplayManager = new OverlayDisplayManager(this, view -> {

            Intent intent = new Intent(FuriganaService.this, FuriganaActivity.class);

            ClipData clip = clipboardManager.getPrimaryClip();

            try {

                CharSequence data = clip.getItemAt(0).getText();
                String text = String.valueOf(data);
                String furigana = TokenizerHelper.getTextWithFurigana(tokenizer, text);

                intent.putExtra(FuriganaActivity.TEXT_EXTRA_FIELD, text);
                intent.putExtra(FuriganaActivity.FURIGANA_EXTRA_FIELD, furigana);

            } catch (Exception e) {
                e.printStackTrace();
                intent.putExtra(FuriganaActivity.ERROR_EXTRA_FIELD,"Unsupported");
            }

            startActivity(intent);

        });

        clipboardListener = () -> {

            //Bypass browsers bug that copies text twice.
            clipboardManager.removePrimaryClipChangedListener(clipboardListener);
            new Handler().postDelayed(() -> clipboardManager.addPrimaryClipChangedListener(clipboardListener), 500);

            overlayDisplayManager.showView();

        };
        clipboardManager.addPrimaryClipChangedListener(clipboardListener);

        new Thread(() -> {

            try {
                tokenizer = new Tokenizer();
            } catch (OutOfMemoryError e) {
                Toast.makeText(getApplicationContext(),R.string.no_memory,Toast.LENGTH_LONG).show();
                stopSelf();
            }

            startForeground(NOTIFICATION_ID, notificationManager.buildServiceNotification());

            serviceStatusRepository.setStatus(ServiceStatus.RUNNING);

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



    @Override
    public void onDestroy() {
        super.onDestroy();

        tokenizer = null;

        if (clipboardManager!=null)
            clipboardManager.removePrimaryClipChangedListener(clipboardListener);

        serviceStatusRepository.setStatus(ServiceStatus.STOPPED);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
