package com.example.iccet2020;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ZapicDoctor> arrayList;
    private Chronometer chronometer;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private DatabaseReference myRef3;
    private String date;
    private FirebaseDatabase mFirebaseDatabase;
    private boolean flag1 = true;
    private boolean flag2 = true;
    private ArrayList<ZapicDoctor> zapicDoctorLast = new ArrayList<>();
    private ArrayList<ZapicDoctor> zapicDoctorNow = new ArrayList<>();
    private ArrayList<DoctorHelper> arrayListKey = new ArrayList<>();
    private Shifr shifr = new Shifr();
    private String doctor;
    private long time = 0;
    boolean flag4 = true;
    int timeBr = 0;

    public MyAdapter(Context ct, ArrayList<ZapicDoctor> aL, Chronometer mChronometr,
                     DatabaseReference databaseReference, String date1, FirebaseDatabase firebaseDatabase,
                     String doctor){
        context = ct;
        arrayList = aL;
        chronometer = mChronometr;
        myRef = databaseReference;
        myRef2 = databaseReference;
        date = date1;
        mFirebaseDatabase = firebaseDatabase;
        this.doctor = doctor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ZapicDoctor zapicDoctor = arrayList.get(position);
        holder.time.setText("Время: " + zapicDoctor.getTime());
        holder.fio.setText("ФИО: " + zapicDoctor.getLastname() + " " +
                zapicDoctor.getName() + " " + zapicDoctor.getMiddlename());
         holder.mainLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                date = removePunct2(date);
                flag1 = true;
                flag2 = true;
                chronometer.stop();
                flag4 = true;
                String q = shifr.dehifator(zapicDoctor.getDoctor());
                String q1 = q.substring(1);
                System.out.println(q1 + " pfvmlfpdofkvjkofdkmvkfl,c v");
                myRef3 = mFirebaseDatabase.getReference("User").child("srednTime");
                myRef3.orderByChild(q1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                String br =  snapshot1.getValue(String.class);
                                String br1 = shifr.dehifator(br);
                                br1 = br1.substring(1);
                                timeBr = Integer.parseInt(br1);
                                System.out.println(timeBr + " TIME BR");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (time != 0 && flag4){
                    myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(q1).child(date).child(zapicDoctor.getTime());
                    CalculatingTimeSr calculatingTimeSr = new CalculatingTimeSr(shifr.hifr_zezarya(date), shifr.hifr_zezarya(String.valueOf(time)));
                    myRef3.setValue(calculatingTimeSr);

                    flag4 = true;
                    myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(q1).child("srTime");
                    myRef3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && flag4) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(dataSnapshot.getValue(String.class));
                                    srTime.setTime(dataSnapshot.getValue(String.class));
                                    System.out.println("adasdasdasfqwfqw" + srTime.getTime());

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    String plusTime = shifr.dehifator(calculatingTimeSr.getTime());
                                    plusTime = plusTime.substring(1);
                                    sr = sr + Integer.parseInt(plusTime);
                                    String sq = String.valueOf(sr);
                                    SrTime srTime2 = new SrTime();
                                    srTime2.setTime(shifr.hifr_zezarya(sq));

                                    sc = sc + 1;
                                    String cq = String.valueOf(sc);
                                    srTime2.setCount(shifr.hifr_zezarya(cq));
                                    myRef3.setValue(srTime2);
                                    myRef3.onDisconnect();
                                    flag4 = false;
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }

                myRef = mFirebaseDatabase.getReference("User").child("Запись " + q1.toLowerCase()).child(date);
                myRef.orderByChild("time").equalTo(zapicDoctor.getTime()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot datas : snapshot.getChildren())
                            {
                                String key = datas.getKey();
                                myRef = mFirebaseDatabase.getReference("User").child("Запись " + q1.toLowerCase()).child(date)
                                        .child(key);
                                myRef.removeValue();
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

                chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        long elapsedMillis = SystemClock.elapsedRealtime()
                                - chronometer.getBase();

                        time = elapsedMillis / 60000;

                        if (elapsedMillis > 1000 && elapsedMillis < 10000)
                        {
                            Toast.makeText(context, "Время приёма заканчивается", Toast.LENGTH_SHORT).show();
                        }
                        // Если время больше 20 минут
                        else if (elapsedMillis > 5000 && flag1)
                        {
                            flag1 = false;
                            String q = shifr.dehifator(zapicDoctor.getDoctor());
                            String q1 = q.substring(1);
                            System.out.println(q1 + " dasgbghnbghbng");
                            System.out.println(date + " dasgbghnbghbng");
                            myRef = mFirebaseDatabase.getReference("User").child("Запись " + q1.toLowerCase()).child(date);
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            ZapicDoctor zapicDoctor = new ZapicDoctor();
                                            zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                                            zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                                            zapicDoctor.setBirthday(dataSnapshot.getValue(ZapicDoctor.class).getBirthday());
                                            zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                                            zapicDoctor.setEmail(dataSnapshot.getValue(ZapicDoctor.class).getEmail());
                                            zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
                                            zapicDoctor.setLastname(dataSnapshot.getValue(ZapicDoctor.class).getLastname());
                                            zapicDoctor.setMiddlename(dataSnapshot.getValue(ZapicDoctor.class).getMiddlename());
                                            zapicDoctor.setName(dataSnapshot.getValue(ZapicDoctor.class).getName());
                                            zapicDoctor.setNomerOMS(dataSnapshot.getValue(ZapicDoctor.class).getNomerOMS());
                                            zapicDoctor.setPhone(dataSnapshot.getValue(ZapicDoctor.class).getPhone());
                                            zapicDoctor.setSeriaOMS(dataSnapshot.getValue(ZapicDoctor.class).getSeriaOMS());
                                            zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                                            zapicDoctor.setCoutnChangeTime(dataSnapshot.getValue(ZapicDoctor.class).getCoutnChangeTime());
                                            zapicDoctor.setKey(dataSnapshot.getValue(ZapicDoctor.class).getKey());
                                            String time = zapicDoctor.getTime();
                                            String resultTime = сalculatingTime(time);
                                            ZapicDoctor zapicDoctor2 = new ZapicDoctor(zapicDoctor.getLastname(),
                                                    zapicDoctor.getName(), zapicDoctor.getMiddlename(), zapicDoctor.getBirthday(),
                                                    zapicDoctor.getSnils(), zapicDoctor.getEmail(), zapicDoctor.getPhone(),
                                                    zapicDoctor.getSeriaOMS(), zapicDoctor.getNomerOMS(), zapicDoctor.getDoctor(),
                                                    zapicDoctor.getData(), resultTime, zapicDoctor.getKabinet(), zapicDoctor.getCoutnChangeTime(), zapicDoctor.getKey());

                                            myRef = mFirebaseDatabase.getReference("User").child("Запись " + q1.toLowerCase()).child(date).child(zapicDoctor2.getKey()).child("time");
                                            myRef.setValue(resultTime);
                                            System.out.println(resultTime + " resulttime");
                                            char resultDoctor = zapicDoctor2.getDoctor().charAt(0);
                                            String resultDoctor2 = String.valueOf(resultDoctor);
                                            byte index = (byte) zapicDoctor2.getDoctor().length();
                                            String finishDoctor = resultDoctor2.toUpperCase() + zapicDoctor2.getDoctor().substring(1, index);
                                            doctor = finishDoctor;
                                            doctor = doctor.substring(0, 1).toUpperCase() + doctor.substring(1).toLowerCase();

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            myRef2 = mFirebaseDatabase.getReference("User").child(q1).child(date);
                            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                        {
                                            Shedule shedule = new Shedule();

                                            shedule.setDate(dataSnapshot1.getValue(Shedule.class).getDate());
                                            shedule.setTime(dataSnapshot1.getValue(Shedule.class).getTime());
                                            String resultTime2 = сalculatingTime(shedule.getTime());
                                            String secondNum = resultTime2.substring(0, 2);
                                            int s = Integer.parseInt(secondNum);
                                            if (s >= 20){
                                                myRef2.child(dataSnapshot1.getKey()).removeValue();
                                            }else {
                                                myRef2.child(dataSnapshot1.getKey()).child("time").setValue(resultTime2);
                                                String dateFinaly = date.substring(0, 2) + "." + date.substring(2, 4) + "." + date.substring(4);
                                                System.out.println(date);
                                                myRef2.child(dataSnapshot1.getKey()).child("date").setValue(dateFinaly);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            notifyDataSetChanged();
                            // если время больше 30 минут
                        }else if (elapsedMillis > 1800000 && flag2) {
                            flag2 = false;
                            String q = shifr.dehifator(zapicDoctor.getDoctor());
                            String q1 = q.substring(1);
                            myRef = mFirebaseDatabase.getReference("User").child("Запись " + q1.toLowerCase()).child(date);
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            ZapicDoctor zapicDoctor = new ZapicDoctor();
                                            zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                                            zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                                            zapicDoctor.setBirthday(dataSnapshot.getValue(ZapicDoctor.class).getBirthday());
                                            zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                                            zapicDoctor.setEmail(dataSnapshot.getValue(ZapicDoctor.class).getEmail());
                                            zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
                                            zapicDoctor.setLastname(dataSnapshot.getValue(ZapicDoctor.class).getLastname());
                                            zapicDoctor.setMiddlename(dataSnapshot.getValue(ZapicDoctor.class).getMiddlename());
                                            zapicDoctor.setName(dataSnapshot.getValue(ZapicDoctor.class).getName());
                                            zapicDoctor.setNomerOMS(dataSnapshot.getValue(ZapicDoctor.class).getNomerOMS());
                                            zapicDoctor.setPhone(dataSnapshot.getValue(ZapicDoctor.class).getPhone());
                                            zapicDoctor.setSeriaOMS(dataSnapshot.getValue(ZapicDoctor.class).getSeriaOMS());
                                            zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                                            zapicDoctor.setCoutnChangeTime(dataSnapshot.getValue(ZapicDoctor.class).getCoutnChangeTime());
                                            zapicDoctor.setKey(dataSnapshot.getValue(ZapicDoctor.class).getKey());
                                            String time = zapicDoctor.getTime();
                                            String resultTime = сalculatingTime(time);
                                            ZapicDoctor zapicDoctor2 = new ZapicDoctor(zapicDoctor.getLastname(),
                                                    zapicDoctor.getName(), zapicDoctor.getMiddlename(), zapicDoctor.getBirthday(),
                                                    zapicDoctor.getSnils(), zapicDoctor.getEmail(), zapicDoctor.getPhone(),
                                                    zapicDoctor.getSeriaOMS(), zapicDoctor.getNomerOMS(), zapicDoctor.getDoctor(),
                                                    zapicDoctor.getData(), resultTime, zapicDoctor.getKabinet(), zapicDoctor.getCoutnChangeTime(), zapicDoctor.getKey());

                                            myRef.child(zapicDoctor2.getKey()).child("time").setValue(resultTime);
                                            char resultDoctor = zapicDoctor2.getDoctor().charAt(0);
                                            String resultDoctor2 = String.valueOf(resultDoctor);
                                            byte index = (byte) zapicDoctor2.getDoctor().length();
                                            String finishDoctor = resultDoctor2.toUpperCase() + zapicDoctor2.getDoctor().substring(1, index);
                                            doctor = finishDoctor;
                                            doctor = doctor.substring(0, 1).toUpperCase() + doctor.substring(1).toLowerCase();

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            myRef2 = mFirebaseDatabase.getReference("User").child(q1).child(date);
                            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                        {
                                            Shedule shedule = new Shedule();

                                            shedule.setDate(dataSnapshot1.getValue(Shedule.class).getDate());
                                            shedule.setTime(dataSnapshot1.getValue(Shedule.class).getTime());
                                            String resultTime2 = сalculatingTime(shedule.getTime());
                                            String secondNum = resultTime2.substring(0, 2);
                                            int s = Integer.parseInt(secondNum);
                                            if (s >= 20){
                                                myRef2.child(dataSnapshot1.getKey()).removeValue();
                                            }else {
                                                myRef2.child(dataSnapshot1.getKey()).child("time").setValue(resultTime2);
                                                String dateFinaly = date.substring(0, 2) + "." + date.substring(2, 4) + "." + date.substring(4);
                                                System.out.println(date);
                                                myRef2.child(dataSnapshot1.getKey()).child("date").setValue(dateFinaly);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        MaterialTextView time, fio;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.timeZapic);
            fio = itemView.findViewById(R.id.fio);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }


    private String сalculatingTime(String time) {
        String resultTime = null;

        String timeHour = time.substring(0, 2);
        String timeMinute = time.substring(3, 5);

        int tMinute = Integer.parseInt(timeMinute) + timeBr;

        if (tMinute > 59)
        {
            String h = null;
            String m = null;
            int tHour = Integer.parseInt(timeHour) + 1;
            tMinute = tMinute - 60;

            if (tMinute < 10)
            {
                m = "0" + tMinute;
            }else {
                m = String.valueOf(tMinute);
            }

            if (tHour < 10)
            {
                h = "0" + tHour;
            }else {
                h = String.valueOf(tHour);
            }

            resultTime = h + ":" + m;
        }else
        {
            String h = null;
            String m = null;
            int tHour = Integer.parseInt(timeHour);

            if (tMinute < 10)
            {
                m = "0" + tMinute;
            }else {
                m = String.valueOf(tMinute);
            }

            if (tHour < 10)
            {
                h = "0" + tHour;
            }else {
                h = String.valueOf(tHour);
            }

            resultTime = h + ":" + m;
        }

        return resultTime;
    }
}
