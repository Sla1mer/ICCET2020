package com.example.iccet2020;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TakeNoteActivity extends AppCompatActivity {

    private EditText surname, name, name_of_father, data, snils, email, phone, polis, polis_number;
    private Button registration_btn, openCalendar;
    private ListView listView;
    private ArrayList<Shedule> shedule2;
    private ArrayList<String> shedule3;
    private ArrayAdapter<String> adapter;
    private String chosheDoctor;

    private ArrayList countries;
    private ArrayAdapter adapterForSpinner;
    private Spinner spinner;
    private String userID;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference myRef3;
    private DatabaseReference myRef4;
    private DatabaseReference myRef2;
    private DatabaseReference myRef5;
    private String USER_KEY = "User";
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    private Timer timer = new Timer();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String date = " ";
    private DatabaseReference mDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_note);

        init();//вызов метода
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a7e7fc")));
        countries = new ArrayList<>();
        countries.add("Выберите врача");
        countries.add("Отоларинголог");
        countries.add("Терапевт");
        countries.add("Хирург");
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3d7894")));
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

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                if (String.valueOf(day).length() == 1)
                {
                    date = ("0" + day + String.valueOf(month) + year);
                }else if (String.valueOf(month).length() == 1)
                {
                    date = (day + "0" + String.valueOf(month) + year);
                }else {
                    date = ("0" + day + "0" + String.valueOf(month) + year);
                }
                date = removePunct2(date);

                if (chosheDoctor.equals("Хирург")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Хирург").child(date);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                } else if (chosheDoctor.equals("Терапевт")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Терапевт").child(date);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                } else if (chosheDoctor.equals("Отоларинголог")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Отоларинголог").child(date);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                }
                System.out.println(shedule2);

            }
        };


        openCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TakeNoteActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterForSpinner);

        ArrayList<String> arrayList = new ArrayList<>();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDataBase.child("User").child(currentUser.getUid());
        System.out.println(currentUser.getUid());
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (arrayList.size() == 0) {
                            User user = new User();
                            user.setBirhday(dataSnapshot.getValue(User.class).getBirhday());
                            data.setText(user.getBirhday());
                            arrayList.add("dasd");
                            user.setEmail(dataSnapshot.getValue(User.class).getEmail());
                            email.setText(user.getEmail());
                            user.setFirstname(dataSnapshot.getValue(User.class).getFirstname());
                            name.setText(user.getFirstname());
                            user.setLastname(dataSnapshot.getValue(User.class).getLastname());
                            surname.setText(user.getLastname());
                            user.setMiddlename(dataSnapshot.getValue(User.class).getMiddlename());
                            name_of_father.setText(user.getMiddlename());
                            user.setNomerOMS(dataSnapshot.getValue(User.class).getNomerOMS());
                            polis_number.setText(user.getNomerOMS());
                            user.setPhone(dataSnapshot.getValue(User.class).getPhone());
                            phone.setText(user.getPhone());
                            user.setSeriaOMS(dataSnapshot.getValue(User.class).getSeriaOMS());
                            polis.setText(user.getSeriaOMS());
                            user.setSnils(dataSnapshot.getValue(User.class).getSnils());
                            snils.setText(user.getSnils());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastname = surname.getText().toString();
                String Name = name.getText().toString();
                String middlename = name_of_father.getText().toString();
                String birthday = data.getText().toString();
                String Snils = snils.getText().toString();
                String Email = email.getText().toString();
                String Phone = phone.getText().toString();
                String seriaOMS = polis.getText().toString();
                String nomerOMS = polis_number.getText().toString();
                String doctor = chosheDoctor;
                int index = listView.getCheckedItemPosition();
                Shedule shedule = shedule2.get(index);
                String date = shedule.getDate();
                String time = shedule.getTime();
                date = removePunct2(date);

                ZapicDoctor zapicDoctor = null;

                if (doctor.equals("Отоларинголог")) {
                    zapicDoctor = new ZapicDoctor(lastname, Name, middlename, birthday, Snils,
                            Email, Phone, seriaOMS, nomerOMS, doctor, shedule.date, shedule.time, "1", "0", shedule.time);
                } else if (doctor.equals("Терапевт")) {
                    zapicDoctor = new ZapicDoctor(lastname, Name, middlename, birthday, Snils,
                            Email, Phone, seriaOMS, nomerOMS, doctor, shedule.date, shedule.time, "2", "0", shedule.time);
                } else if (doctor.equals("Хирург")) {
                    zapicDoctor = new ZapicDoctor(lastname, Name, middlename, birthday, Snils,
                            Email, Phone, seriaOMS, nomerOMS, doctor, shedule.date, shedule.time, "3", "0", shedule.time);
                }

                if (doctor.equals("Отоларинголог")) {
                    myRef3.child("Запись отоларинголог").child(date).child(time).setValue(zapicDoctor);
                    myRef4 = mFirebaseDatabase.getReference("User").child(zapicDoctor.getDoctor()).child(date);
                    ZapicDoctor finalZapicDoctor = zapicDoctor;
                    String finalDate = date;
                    System.out.println(finalDate);
                    System.out.println(finalZapicDoctor.getTime());
                    System.out.println(zapicDoctor.getDoctor());
                    myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                for (DataSnapshot datas : snapshot.getChildren())
                                {
                                    String key = datas.getKey();
                                    System.out.println(key);
                                    myRef4 = mFirebaseDatabase.getReference("User").child(finalZapicDoctor.getDoctor()).child(finalDate)
                                            .child(key);
                                    myRef4.removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), com.example.iccet2020.Services.Service.class);
                    intent.putExtra("date", zapicDoctor.getData());
                    intent.putExtra("time", zapicDoctor.getTime());
                    intent.putExtra("doctor", zapicDoctor.getDoctor());
                    intent.putExtra("snils", zapicDoctor.getSnils());
                    startForegroundService(intent);
                } else if (doctor.equals("Терапевт")) {
                    myRef3.child("Запись терапевт").child(date).child(time).setValue(zapicDoctor);
                    myRef4 = mFirebaseDatabase.getReference("User").child(zapicDoctor.getDoctor()).child(date);
                    ZapicDoctor finalZapicDoctor = zapicDoctor;
                    String finalDate = date;
                    System.out.println(finalDate);
                    System.out.println(finalZapicDoctor.getTime());
                    System.out.println(zapicDoctor.getDoctor());
                    myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                for (DataSnapshot datas : snapshot.getChildren())
                                {
                                    String key = datas.getKey();
                                    System.out.println(key);
                                    myRef4 = mFirebaseDatabase.getReference("User").child(finalZapicDoctor.getDoctor()).child(finalDate)
                                            .child(key);
                                    myRef4.removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), com.example.iccet2020.Services.Service.class);
                    intent.putExtra("date", zapicDoctor.getData());
                    intent.putExtra("time", zapicDoctor.getTime());
                    intent.putExtra("doctor", zapicDoctor.getDoctor());
                    intent.putExtra("snils", zapicDoctor.getSnils());
                    startForegroundService(intent);
                } else if (doctor.equals("Хирург")) {
                    myRef3.child("Запись хирург").child(date).child(time).setValue(zapicDoctor);
                    myRef4 = mFirebaseDatabase.getReference("User").child(zapicDoctor.getDoctor()).child(date);
                    ZapicDoctor finalZapicDoctor = zapicDoctor;
                    String finalDate = date;
                    System.out.println(finalDate);
                    System.out.println(finalZapicDoctor.getTime());
                    System.out.println(zapicDoctor.getDoctor());
                    myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                for (DataSnapshot datas : snapshot.getChildren())
                                {
                                    String key = datas.getKey();
                                    System.out.println(key);
                                    myRef4 = mFirebaseDatabase.getReference("User").child(finalZapicDoctor.getDoctor()).child(finalDate)
                                            .child(key);
                                    myRef4.removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), com.example.iccet2020.Services.Service.class);
                    intent.putExtra("date", zapicDoctor.getData());
                    intent.putExtra("time", zapicDoctor.getTime());
                    intent.putExtra("doctor", zapicDoctor.getDoctor());
                    intent.putExtra("snils", zapicDoctor.getSnils());
                    startForegroundService(intent);
                }
            }
        });

        myRef2 = mFirebaseDatabase.getReference();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosheDoctor = ((TextView) view).getText().toString();

                date = removePunct2(date);

                if (chosheDoctor.equals("Хирург")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Хирург").child(date);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                } else if (chosheDoctor.equals("Терапевт")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Терапевт").child(date);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                } else if (chosheDoctor.equals("Отоларинголог")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Отоларинголог").child(date);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                }
                System.out.println(shedule2);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    //иницилизация

    private void init(){
        spinner = findViewById(R.id.spinner);

        surname = findViewById(R.id.surname);
        name = findViewById(R.id.name);
        name_of_father = findViewById(R.id.name_of_father);
        data = findViewById(R.id.data);
        snils = findViewById(R.id.snils);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        polis = findViewById(R.id.polis);
        polis_number = findViewById(R.id.polis_number);
        myRef3 = FirebaseDatabase.getInstance().getReference(USER_KEY);
        myRef4 = FirebaseDatabase.getInstance().getReference(USER_KEY);
        myRef5 = FirebaseDatabase.getInstance().getReference(USER_KEY);
        openCalendar = findViewById(R.id.openCalendar);

        registration_btn = findViewById(R.id.registration_btn);
        // получаем экземпляр элемента ListView
        listView = findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        shedule2 = new ArrayList<Shedule>();
        shedule3 = new ArrayList<String>();

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, shedule3);

        // Привяжем массив через адаптер к ListView
        listView.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = FirebaseAuth.getInstance().getUid();
    }

    private void showData(DataSnapshot snapshot) {
        for (DataSnapshot ds : snapshot.getChildren()){
            User uInfo = new User();
            uInfo.setFirstname(ds.child(userID).getValue(User.class).getFirstname());
            uInfo.setLastname(ds.child(userID).getValue(User.class).getLastname());
            uInfo.setMiddlename(ds.child(userID).getValue(User.class).getMiddlename());
            uInfo.setBirhday(ds.child(userID).getValue(User.class).getBirhday());
            uInfo.setSnils(ds.child(userID).getValue(User.class).getSnils());
            uInfo.setEmail(ds.child(userID).getValue(User.class).getEmail());
            uInfo.setPhone(ds.child(userID).getValue(User.class).getPhone());
            uInfo.setSeriaOMS(ds.child(userID).getValue(User.class).getSeriaOMS());
            uInfo.setNomerOMS(ds.child(userID).getValue(User.class).getNomerOMS());

            surname.setText(uInfo.getLastname());
            name.setText(uInfo.getFirstname());
            name_of_father.setText(uInfo.getMiddlename());
            data.setText(uInfo.getBirhday());
            snils.setText(uInfo.getSnils());
            email.setText(uInfo.getEmail());
            phone.setText(uInfo.getPhone());
            polis.setText(uInfo.getSeriaOMS());
            polis_number.setText(uInfo.getNomerOMS());
        }
    }
}