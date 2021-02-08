package com.example.iccet2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class DoctorActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private String USER_KEY = "User";
    private FirebaseDatabase mFirebaseDatabase;
    private Chronometer mChronometer;
    private ArrayList<ZapicDoctor> zapicDoctorsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private ArrayList countries;
    private ArrayAdapter adapterForSpinner;
    private Spinner spinner;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String chosheDoctor = " ";
    private MaterialButton takeDate, startTimer, stopTimer;
    private MaterialTextView date;
    private Button btnExit, addNoteBtn;
    private long time2 = 0;
    private Shifr shifr = new Shifr();
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        myRef = FirebaseDatabase.getInstance().getReference(USER_KEY);
        myRef2 = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChronometer = findViewById(R.id.chronometer);
        spinner = findViewById(R.id.spinner);
        takeDate = findViewById(R.id.takeDate);
        startTimer = findViewById(R.id.startTimer);
        stopTimer = findViewById(R.id.stopTimer);
        date = findViewById(R.id.date);
        myAdapter = new MyAdapter(this, zapicDoctorsList, mChronometer, myRef2, date.getText().toString(), mFirebaseDatabase, chosheDoctor);
        btnExit = findViewById(R.id.exit);
        addNoteBtn = findViewById(R.id.addNoteBtn);
        btnExit = findViewById(R.id.exit);
        addNoteBtn = findViewById(R.id.addNoteBtn);


        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();

                mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        long elapsedMillis = SystemClock.elapsedRealtime()
                                - chronometer.getBase();

                        time2 = elapsedMillis / 60000;
                    }
                });
            }
        });

        stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChronometer.stop();

                String choseDoctor2 = chosheDoctor.toLowerCase();
                myRef = mFirebaseDatabase.getReference("User").child("Запись " + choseDoctor2).child(removePunct2(date.getText().toString()));

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                myRef.child(dataSnapshot1.getKey()).child("time").setValue(resultTime2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                chosheDoctor = chosheDoctor.substring(0, 1).toUpperCase() + chosheDoctor.substring(1).toLowerCase();
                myRef2 = mFirebaseDatabase.getReference("User").child(chosheDoctor).child(removePunct2(date.getText().toString()));
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
                                myRef2.child(dataSnapshot1.getKey()).child("time").setValue(resultTime2);
                                String dateFinaly = date.getText().toString().substring(0, 2) + "." + date.getText().toString().substring(2, 4) + "." + date.getText().toString().substring(4);
                                myRef2.child(dataSnapshot1.getKey()).child("date").setValue(dateFinaly);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.exit){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        countries = new ArrayList<>();
        countries.add("Выберите вашу должность");
        countries.add("отоларинголог");
        countries.add("терапевт");
        countries.add("хирург");

        adapterForSpinner = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, countries) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }




            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterForSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosheDoctor = ((TextView) view).getText().toString();
                getData(chosheDoctor, date.getText().toString());
                myAdapter = new MyAdapter(getApplicationContext(), zapicDoctorsList, mChronometer, myRef2, date.getText().toString(), mFirebaseDatabase, chosheDoctor);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date2 = day + "." + month + "." + year;
                String m = String.valueOf(month);
                String d = String.valueOf(day);
                if (String.valueOf(day).length() == 1)
                {
                    d = ("0" + day);
                }

                if (String.valueOf(month).length() == 1)
                {
                    m = ("0" + month);
                }

                date2 = d + "." + m + "." + year;
                date.setText(date2);
                getData(chosheDoctor, date.getText().toString());
                myAdapter = new MyAdapter(getApplicationContext(), zapicDoctorsList, mChronometer, myRef2, date.getText().toString(), mFirebaseDatabase, chosheDoctor);
                recyclerView.setAdapter(myAdapter);
            }
        };

        takeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DoctorActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        recyclerView = findViewById(R.id.recyclerZapic);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    private String сalculatingTime(String time) {
        String resultTime = null;

        String timeHour = time.substring(0, 2);
        String timeMinute = time.substring(3, 5);

        int tMinute = (int) (Integer.parseInt(timeMinute) + time2);

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

    private void getData(String pathDoctor, String date)
    {
        date = removePunct2(date);
        myRef2 = mFirebaseDatabase.getReference("User").child("Запись " + pathDoctor.toLowerCase()).child(date);

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                zapicDoctorsList.clear();
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

                    zapicDoctor.setKabinet(shifr.dehifator(zapicDoctor.getKabinet()));
                    zapicDoctor.setSnils(shifr.dehifator(zapicDoctor.getSnils()));
                    zapicDoctor.setData(shifr.dehifator(zapicDoctor.getData()));
                    zapicDoctor.setDoctor(shifr.dehifator(zapicDoctor.getDoctor()));
                    zapicDoctor.setKey(shifr.dehifator(zapicDoctor.getKey()));
                    zapicDoctor.setCoutnChangeTime(shifr.dehifator(zapicDoctor.getCoutnChangeTime()));
                    zapicDoctor.setSeriaOMS(shifr.dehifator(zapicDoctor.getSeriaOMS()));
                    zapicDoctor.setPhone(shifr.dehifator(zapicDoctor.getPhone()));
                    zapicDoctor.setNomerOMS(shifr.dehifator(zapicDoctor.getNomerOMS()));
                    zapicDoctor.setName(shifr.dehifator(zapicDoctor.getName()));
                    zapicDoctor.setMiddlename(shifr.dehifator(zapicDoctor.getMiddlename()));
                    zapicDoctor.setLastname(shifr.dehifator(zapicDoctor.getLastname()));
                    zapicDoctor.setEmail(shifr.dehifatorEmail(zapicDoctor.getEmail()));
                    zapicDoctor.setBirthday(shifr.dehifator(zapicDoctor.getBirthday()));

                    zapicDoctorsList.add(zapicDoctor);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}