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
import com.example.iccet2020.Shifr;
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
    private String timeT = "";
    private String timeX = "";
    private String timeO = "";
    private String snils = "";
    private String date = "";
    private String doctorT = "";
    private String doctorX = "";
    private String doctorO = "";
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    private Shifr shifr = new Shifr();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        date = intent.getStringExtra("date");
        timeT = intent.getStringExtra("timeT");
        timeX = intent.getStringExtra("timeX");
        timeO = intent.getStringExtra("timeO");
        doctorT = intent.getStringExtra("doctorT");
        doctorX = intent.getStringExtra("doctorX");
        doctorO = intent.getStringExtra("doctorO");
        snils = intent.getStringExtra("snils");
        date = removePunct2(date);
        System.out.println(snils);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (doctorO != null)
                {
                    myRef = mFirebaseDatabase.getReference("User").child("Запись " + doctorO.toLowerCase()).child(date);

                    myRef.orderByChild("snils").equalTo(snils).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ZapicDoctor zapicDoctor = new ZapicDoctor();
                                    zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                                    zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                                    zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                                    zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                                    zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
                                    System.out.println(zapicDoctor.getSnils());
                                    System.out.println(zapicDoctor.getTime());

                                    if (zapicDoctor.getSnils().equals(snils)) {
                                        if (timeO.equals(zapicDoctor.getTime())) {

                                        } else {
                                            String doctorq = shifr.dehifator(zapicDoctor.getDoctor());
                                            String secondNum = timeO.substring(0, 2);
                                            int s = Integer.parseInt(secondNum);
                                            if (s >= 20) {
                                                    
                                                String bigText = "Запись к " + doctorq.toLowerCase() + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()) + "\n" + "К сожалению, приём задерживается. Доктор обязательно вас примет. Если для вас это не удобно, вы можете отменить приём и записаться на другую дату.";
                                                Intent intent2 = new Intent(context, MainActivity.class);
                                                PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
                                                Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
                                                        .setContentTitle("Iccet2020")
                                                        .setContentText("Запись к " + shifr.dehifator(zapicDoctor.getDoctor().toLowerCase()) + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()) + "\n" + "К сожалению, приём задерживается. Доктор обязательно вас примет. Если для вас это не удобно, вы можете отменить приём и записаться на другую дату.")
                                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                                                        .setContentIntent(pendingIntent2).build();
                                                startForeground(1, notification2);
                                                System.out.println(timeO);
                                                System.out.println(zapicDoctor.getTime());
                                                timeO = zapicDoctor.getTime();
                                            } else {
                                                String bigText = "Запись к " + doctorq.toLowerCase() + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet());
                                                Intent intent2 = new Intent(context, MainActivity.class);
                                                PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
                                                Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
                                                        .setContentTitle("Iccet2020")
                                                        .setContentText("Запись к " + shifr.dehifator(zapicDoctor.getDoctor().toLowerCase()) + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()))
                                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                                                        .setContentIntent(pendingIntent2).build();
                                                startForeground(1, notification2);
                                                System.out.println(timeO);
                                                System.out.println(zapicDoctor.getTime());
                                                timeO = zapicDoctor.getTime();
                                            }
                                        }
                                    }else {
                                        stopSelf();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (doctorT != null)
                {
                    myRef = mFirebaseDatabase.getReference("User").child("Запись " + doctorT.toLowerCase()).child(date);

                    myRef.orderByChild("snils").equalTo(snils).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ZapicDoctor zapicDoctor = new ZapicDoctor();
                                    zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                                    zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                                    zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                                    zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                                    zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
                                    System.out.println(zapicDoctor.getSnils());
                                    System.out.println(zapicDoctor.getTime());

                                    if (zapicDoctor.getSnils().equals(snils)) {
                                        if (timeT.equals(zapicDoctor.getTime())) {

                                        } else {
                                            String secondNum = timeT.substring(0, 2);
                                            int s = Integer.parseInt(secondNum);
                                            String doctorq = shifr.dehifator(zapicDoctor.getDoctor());
                                            if (s >= 20) {
                                                String bigText = "Запись к " + doctorq.toLowerCase() + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()) + "\n" + "К сожалению, приём задерживается. Доктор обязательно вас примет. Если для вас это не удобно, вы можете отменить приём и записаться на другую дату.";
                                                Intent intent2 = new Intent(context, MainActivity.class);
                                                PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
                                                Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
                                                        .setContentTitle("Iccet2020")
                                                        .setContentText("Запись к " + shifr.dehifator(zapicDoctor.getDoctor().toLowerCase()) + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()) + "\n" + "К сожалению, приём задерживается. Доктор обязательно вас примет. Если для вас это не удобно, вы можете отменить приём и записаться на другую дату.")
                                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                                                        .setContentIntent(pendingIntent2).build();
                                                startForeground(1, notification2);
                                                System.out.println(timeT);
                                                System.out.println(zapicDoctor.getTime());
                                                timeT = zapicDoctor.getTime();
                                            } else {
                                                String bigText = "Запись к " + doctorq.toLowerCase() + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet());
                                                Intent intent2 = new Intent(context, MainActivity.class);
                                                PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
                                                Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
                                                        .setContentTitle("Iccet2020")
                                                        .setContentText("Запись к " + shifr.dehifator(zapicDoctor.getDoctor().toLowerCase()) + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()))
                                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                                                        .setContentIntent(pendingIntent2).build();
                                                startForeground(2, notification2);
                                                System.out.println(timeT);
                                                System.out.println(zapicDoctor.getTime());
                                                timeT = zapicDoctor.getTime();
                                            }
                                        }
                                    }else {
                                        stopSelf();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (doctorX != null)
                {
                    myRef = mFirebaseDatabase.getReference("User").child("Запись " + doctorX.toLowerCase()).child(date);

                    myRef.orderByChild("snils").equalTo(snils).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ZapicDoctor zapicDoctor = new ZapicDoctor();
                                    zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                                    zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                                    zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                                    zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                                    zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
                                    System.out.println(zapicDoctor.getSnils());
                                    System.out.println(zapicDoctor.getTime());

                                    if (zapicDoctor.getSnils().equals(snils)) {
                                        if (timeX.equals(zapicDoctor.getTime())) {

                                        } else {
                                            String doctorq = shifr.dehifator(zapicDoctor.getDoctor());
                                            String secondNum = timeX.substring(0, 2);
                                            int s = Integer.parseInt(secondNum);
                                            if (s >= 20) {

                                                String bigText = "Запись к " + doctorq.toLowerCase() + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()) + "\n" + "К сожалению, приём задерживается. Доктор обязательно вас примет. Если для вас это не удобно, вы можете отменить приём и записаться на другую дату.";
                                                Intent intent2 = new Intent(context, MainActivity.class);
                                                PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
                                                Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
                                                        .setContentTitle("Iccet2020")
                                                        .setContentText("Запись к " + shifr.dehifator(zapicDoctor.getDoctor().toLowerCase()) + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()) + "\n" + "К сожалению, приём задерживается. Доктор обязательно вас примет. Если для вас это не удобно, вы можете отменить приём и записаться на другую дату.")
                                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                                                        .setContentIntent(pendingIntent2).build();
                                                startForeground(1, notification2);
                                                System.out.println(timeX);
                                                System.out.println(zapicDoctor.getTime());
                                                timeX = zapicDoctor.getTime();
                                            } else {
                                                String bigText = "Запись к " + doctorq.toLowerCase() + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet());
                                                Intent intent2 = new Intent(context, MainActivity.class);
                                                PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
                                                Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
                                                        .setContentTitle("Iccet2020")
                                                        .setContentText("Запись к " + shifr.dehifator(zapicDoctor.getDoctor().toLowerCase()) + "у" + "\n" + "Дата: " + shifr.dehifator(zapicDoctor.getData()) + "\n" + "Время: " + zapicDoctor.getTime() + "\n" + "Кабинет: " + shifr.dehifator(zapicDoctor.getKabinet()))
                                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                                                        .setContentIntent(pendingIntent2).build();
                                                startForeground(3, notification2);
                                                System.out.println(timeX);
                                                System.out.println(zapicDoctor.getTime());
                                                timeX = zapicDoctor.getTime();
                                            }
                                        }
                                    }else {
                                        stopSelf();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        }, 0, 5000);

        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
        Notification notification2 = new NotificationCompat.Builder(context, "ChannelId1")
                .setContentTitle("Iccet2020")
                .setContentText("Идёт отслеживание времени приёма")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent2).build();
        startForeground(1, notification2);
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
