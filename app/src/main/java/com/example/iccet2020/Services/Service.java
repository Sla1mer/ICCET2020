package com.example.iccet2020.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;

public class Service extends android.app.Service {

    private Timer timer = new Timer();
    private Context context = this;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // Здесь будет код сравнивая данных с сервера и будет вызываться push-notification если нужно
//                Intent intent2 = new Intent(context, MainActivity.class);
//                PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
//                Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
//                        .setContentTitle("Iccet2020")
//                        .setContentText("Foreground Notification")
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentIntent(pendingIntent2).build();
//                startForeground(1, notification2);
            }
        }, 0, 600000);;
        return START_STICKY;
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
