package ua.syt0r.furiganit.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import ua.syt0r.furiganit.R;
import static ua.syt0r.furiganit.service.FuriganaService.SERVICE_STOP;

public class ServiceNotificationManager {

    private static final String CHANNEL_ID = "ua.syt0r.furigan_it";
    private static final String CHANNEL_NAME = "FuriganIt Service";

    private NotificationCompat.Builder builder;

    public ServiceNotificationManager(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(context);

        builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_ik)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(context.getResources().getString(R.string.service_is_running))
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent stopServiceIntent = new Intent(context, FuriganaService.class);
        stopServiceIntent.setAction(SERVICE_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_stat_ik, context.getResources().getString(R.string.stop),
                pendingIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null)
            manager.createNotificationChannel(channel);
    }

    public Notification buildServiceNotification() {
        return builder.build();
    }

}
