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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private Button btnExit;
    private long time2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3d7894")));

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
                mChronometer.setBase(SystemClock.elapsedRealtime());

                myRef = mFirebaseDatabase.getReference("User").child("Запись " + chosheDoctor).child(date.getText().toString());
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
                                chosheDoctor = finishDoctor;
                                chosheDoctor = chosheDoctor.substring(0, 1).toUpperCase() + chosheDoctor.substring(1).toLowerCase();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                chosheDoctor = chosheDoctor.substring(0, 1).toUpperCase() + chosheDoctor.substring(1).toLowerCase();
                myRef2 = mFirebaseDatabase.getReference("User").child(chosheDoctor).child(date.getText().toString());
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
                                if (resultTime2.charAt(0) != '2')
                                {
                                    myRef2.child(dataSnapshot1.getKey()).child("time").setValue(resultTime2);
                                    String dateFinaly = date.getText().toString().substring(0, 2) + "." + date.getText().toString().substring(2, 4) + "." + date.getText().toString().substring(4);
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
                if (String.valueOf(day).length() == 1)
                {
                    date.setText("0" + day + String.valueOf(month) + year);
                }else if (String.valueOf(month).length() == 1)
                {
                    date.setText(day + "0" + String.valueOf(month) + year);
                }else {
                    date.setText("0" + day + "0" + String.valueOf(month) + year);
                }
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

    private String сalculatingTime(String time) {
        String resultTime = null;

        if (time.charAt(0) == '0' && time.charAt(3) != '0') {
            char timeHour = time.charAt(1);
            System.out.println("timeHoSur " + timeHour);
            String timeMinute = time.substring(3, 5);
            int timeMinutePlus = Math.toIntExact(Integer.parseInt(timeMinute) + time2);
            int timeHourPlus = Integer.parseInt(String.valueOf(timeHour));
            String  timeHourPlus2 = null;
            String timeMinutePlus2 = null;

            timeHourPlus2 = String.valueOf(timeHourPlus);
            timeMinutePlus2 = String.valueOf(timeMinutePlus);
            System.out.println("timeMinutePlus " + timeMinutePlus);
            System.out.println("timeHourPlus " + timeHourPlus);
            if (timeMinutePlus > 59) {
                timeMinutePlus = timeMinutePlus - 60;
                timeMinutePlus2 = String.valueOf(timeMinutePlus);
                timeHourPlus = Integer.parseInt(String.valueOf(timeHour)) + 1;
                timeHourPlus2 = String.valueOf(timeHourPlus);
            }

            if (timeHourPlus2.length() == 1 && timeMinutePlus2.length() == 1)
            {
                resultTime = '0' + timeHourPlus2 + ":" + "0" + timeMinutePlus2;
            }else if (timeHourPlus2.length() == 2 && timeMinutePlus2.length() == 1)
            {
                resultTime = timeHourPlus2 + ":" + "0" + timeMinutePlus2;
            }else if (timeHourPlus2.length() == 1 && timeMinutePlus2.length() == 2)
            {
                resultTime = '0' + timeHourPlus2 + ":" + timeMinutePlus2;
            }else if (timeHourPlus2.length() == 2 && timeMinutePlus2.length() == 2)
            {
                resultTime = timeHourPlus2 + ":" + timeMinutePlus2;
            }
            System.out.println(resultTime);
        } else if (time.charAt(0) != '0' && time.charAt(3) == '0')
        {
            String timeHour = time.substring(0, 2);
            System.out.println("timeHoSur " + timeHour);
            char timeMinute = time.charAt(4);
            int timeMinutePlus = Math.toIntExact(Integer.parseInt(String.valueOf(timeMinute)) + time2);
            int timeHourPlus = Integer.parseInt(timeHour);
            System.out.println("timeMinutePlus " + timeMinutePlus);
            System.out.println("timeHourPlus " + timeHourPlus);

            resultTime = timeHourPlus + ":" + timeMinutePlus;
        } else if (time.charAt(0) == '0' && time.charAt(3) == '0')
        {
            System.out.println("ejje " + time);
            char timeHour = time.charAt(1);
            char timeMinute = time.charAt(4);
            System.out.println("timeMinute" + timeMinute);
            System.out.println("timeHourPlus " + timeHour);

            int timeMinutePlus = Integer.parseInt(String.valueOf(timeMinute));
            timeMinutePlus = Math.toIntExact(timeMinutePlus + time2);
            int timeHourPlus = Integer.parseInt(String.valueOf(timeHour));
            System.out.println("dasdsadasdas" + timeHourPlus);

            resultTime = '0' + time.charAt(1) + ":" + timeMinutePlus;

        }else if (time.charAt(0) != '0' && time.charAt(3) != '0')
        {
            String timeHour = time.substring(0, 2);
            String timeMinute = time.substring(3, 5);
            System.out.println("timeMinute" + timeMinute);
            System.out.println("timeHourPlus " + timeHour);

            int timeMinutePlus = Math.toIntExact(Integer.parseInt(timeMinute) + time2);
            int timeHourPlus = Integer.parseInt(timeHour);
            String  timeHourPlus2 = null;
            String timeMinutePlus2 = null;

            timeHourPlus2 = String.valueOf(timeHourPlus);
            timeMinutePlus2 = String.valueOf(timeMinutePlus);
            System.out.println("timeMinutePlus " + timeMinutePlus);
            System.out.println("timeHourPlus " + timeHourPlus);
            if (timeMinutePlus > 59) {
                timeMinutePlus = timeMinutePlus - 60;
                timeMinutePlus2 = String.valueOf(timeMinutePlus);
                timeHourPlus = Integer.parseInt(String.valueOf(timeHour)) + 1;
                timeHourPlus2 = String.valueOf(timeHourPlus);
            }

            if (timeHourPlus2.length() == 1 && timeMinutePlus2.length() == 1)
            {
                resultTime = '0' + timeHourPlus2 + ":" + "0" + timeMinutePlus2;
            }else if (timeHourPlus2.length() == 2 && timeMinutePlus2.length() == 1)
            {
                resultTime = timeHourPlus2 + ":" + "0" + timeMinutePlus2;
            }else if (timeHourPlus2.length() == 1 && timeMinutePlus2.length() == 2)
            {
                resultTime = '0' + timeHourPlus2 + ":" + timeMinutePlus2;
            }else if (timeHourPlus2.length() == 2 && timeMinutePlus2.length() == 2)
            {
                resultTime = timeHourPlus2 + ":" + timeMinutePlus2;
            }
        }

        return resultTime;
    }

    private void getData(String pathDoctor, String date)
    {
        myRef2 = mFirebaseDatabase.getReference("User").child("Запись " + pathDoctor).child(date);

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
                    zapicDoctorsList.add(zapicDoctor);
                    System.out.println(zapicDoctorsList);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}