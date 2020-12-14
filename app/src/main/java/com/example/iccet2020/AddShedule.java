package com.example.iccet2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddShedule extends AppCompatActivity {

    private TextInputEditText date, time, name;
    private MaterialButton send;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef3;
    private DatabaseReference myRef;
    private Shifr shifr = new Shifr();
    int countSr = 0;
    boolean flag4 = true;
    ArrayList<String> arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shedule);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3d7894")));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        send = findViewById(R.id.send);
        name = findViewById(R.id.name);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (date.getText().toString()) {
                    case "1":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               for (DataSnapshot dataSnapshot : snapshot.getChildren())
                               {
                                   String count = dataSnapshot.getValue(String.class);
                                   arrayList.add(count);
                                   myRef3.onDisconnect();
                               }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                        SrTime srTime = new SrTime();
                                        srTime.setCount(arrayList.get(0));
                                        srTime.setTime(arrayList.get(1));

                                        String t = shifr.dehifator(srTime.getTime());
                                        String c = shifr.dehifator(srTime.getCount());
                                        t = t.substring(1);
                                        c = c.substring(1);
                                        int sr = Integer.parseInt(t);
                                        int sc = Integer.parseInt(c);
                                        System.out.println(sr);
                                        System.out.println(sc);
                                        srTimeInt = sr / sc;
                                        System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 32; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                        {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "2":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 29; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "3":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 32; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "4":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 31; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "5":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 32; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "6":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 31; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "7":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 32; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "8":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 31; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "9":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 32; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "10":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 31; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "11":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 31; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    case "12":
                        arrayList.clear();
                        flag4 = true;

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("count").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String count = dataSnapshot.getValue(String.class);
                                    arrayList.add(count);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.orderByChild("time").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String time = dataSnapshot.getValue(String.class);
                                    arrayList.add(time);
                                    myRef3.onDisconnect();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef3 = mFirebaseDatabase.getReference("User").child("calculatingSrTime").child(name.getText().toString()).child("srTime");
                        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int srTimeInt = 0;
                                if (snapshot.exists() && flag4) {
                                    SrTime srTime = new SrTime();
                                    srTime.setCount(arrayList.get(0));
                                    srTime.setTime(arrayList.get(1));

                                    String t = shifr.dehifator(srTime.getTime());
                                    String c = shifr.dehifator(srTime.getCount());
                                    t = t.substring(1);
                                    c = c.substring(1);
                                    int sr = Integer.parseInt(t);
                                    int sc = Integer.parseInt(c);
                                    System.out.println(sr);
                                    System.out.println(sc);
                                    srTimeInt = sr / sc;
                                    System.out.println(srTimeInt);
                                }

                                for (int i = 1; i < 32; i++) {
                                    String time = "08:00";
                                    int c = i;
                                    byte count = 0;
                                    System.out.println(Integer.parseInt(time.substring(0, 2)));
                                    while (Integer.parseInt(time.substring(0, 2) + time.substring(3)) < 1945)
                                    {
                                        if (count != 0){
                                            time = calcuclatingTime(time, srTimeInt);
                                        }else
                                        {
                                            count = (byte) (count + 1);
                                        }
                                        String dateMSG = "";
                                        String nameMSG = name.getText().toString();
                                        System.out.println(i);
                                        if (i < 10) {
                                            dateMSG = "0" + c + '.' + "0" + "1" + '.' + "2020";
                                        } else {
                                            dateMSG = "" + c + '.' + "0" + "1" + '.' + "2020";
                                        }
                                        System.out.println(dateMSG);
                                        dateMSG = removePunct2(dateMSG);
                                        Shedule shedule = new Shedule(dateMSG, time);
                                        mDataBase.child(nameMSG).child(dateMSG).child(time).setValue(shedule);
                                    }
                                }

                                myRef = mFirebaseDatabase.getReference("User").child("srednTime").child(name.getText().toString());
                                myRef.setValue(shifr.hifr_zezarya(String.valueOf(srTimeInt)));
                                String stTime = shifr.hifr_zezarya("0");
                                String count = shifr.hifr_zezarya("0");
                                SrTime srTime5 = new SrTime(stTime, count);
                                mDataBase.child("calculatingSrTime").child(name.getText().toString()).child("srTime").setValue(srTime5);
                                flag4 = false;
                                myRef3.onDisconnect();
                                myRef.onDisconnect();
                                mDataBase.onDisconnect();
                                Toast.makeText(getApplicationContext(), "Записи успешено созданы", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                }
            }
        });
    }

    public String calcuclatingTime(String time, int srTime) {
        String resultTime = null;

        String timeHour = time.substring(0, 2);
        String timeMinute = time.substring(3, 5);

        int tMinute = Integer.parseInt(timeMinute) + srTime;

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

    protected OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }
}