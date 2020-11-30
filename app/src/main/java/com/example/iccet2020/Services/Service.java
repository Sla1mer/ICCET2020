package com.example.iccet2020.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.iccet2020.DoctorActivity;
import com.example.iccet2020.MainActivity;
import com.example.iccet2020.R;
import com.example.iccet2020.Shedule;
import com.example.iccet2020.ZapicDoctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class Service extends android.app.Service {

    private Timer timer = new Timer();
    private Context context = this;
    private String lastTime = "";
    private String time = "";
    private String snils = "";
    private String date = "";
    private String doctor = "";
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        date = intent.getStringExtra("date");
        lastTime = intent.getStringExtra("time");
        time = intent.getStringExtra("time");
        doctor = intent.getStringExtra("doctor");
        snils = intent.getStringExtra("snils");
        date = removePunct2(date);
        System.out.println(snils);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                myRef = mFirebaseDatabase.getReference("User").child("Запись " + doctor.toLowerCase()).child(date);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ZapicDoctor zapicDoctor = new ZapicDoctor();
                            zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                            zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                            zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                            zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                            System.out.println(zapicDoctor.getSnils());
                            System.out.println(zapicDoctor.getTime());

                            if (zapicDoctor.getSnils().equals(snils)){
                                if (lastTime.equals(zapicDoctor.getTime()))
                                {

                                }else {
                                    Intent intent2 = new Intent(context, DoctorActivity.class);
                                    PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
                                    Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
                                            .setContentTitle("Iccet2020")
                                            .setContentText("Запись к " + zapicDoctor.getDoctor().toLowerCase() + "\n" + "Дата: " + zapicDoctor.getData() + "\n" + "Время: " + zapicDoctor.getTime())
                                            .setSmallIcon(R.drawable.ic_launcher_background)
                                            .setContentIntent(pendingIntent2).build();
                                    startForeground(1, notification2);
                                    System.out.println(lastTime);
                                    System.out.println(zapicDoctor.getTime());
                                    lastTime = zapicDoctor.getTime();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
        }, 0, 5000);;
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

    public static String removePunct2(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (PUNCT.indexOf(c) < 0) {
                result.append(c);
            }
        }
        return result.toString();
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
