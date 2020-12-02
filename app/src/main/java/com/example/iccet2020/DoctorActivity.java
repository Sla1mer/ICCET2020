package com.example.iccet2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
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
    private MaterialButton takeDate;
    private MaterialTextView date;


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
        date = findViewById(R.id.date);
        myAdapter = new MyAdapter(this, zapicDoctorsList, mChronometer, myRef2, date.getText().toString(), mFirebaseDatabase, chosheDoctor);



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