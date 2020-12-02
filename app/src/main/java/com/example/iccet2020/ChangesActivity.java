package com.example.iccet2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ChangesActivity extends AppCompatActivity {

    private Button changedBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextInputEditText data, email, name, surname, name_of_father, polis_number, phone, polis, snils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);

        data = findViewById(R.id.data);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        name_of_father = findViewById(R.id.name_of_father);
        polis_number = findViewById(R.id.polis_number);
        phone = findViewById(R.id.phone);
        polis = findViewById(R.id.polis);
        snils = findViewById(R.id.snils);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        changedBtn = findViewById(R.id.changedBtn);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
                if (String.valueOf(day).length() == 1)
                {
                    date = ("0" + day + "." + String.valueOf(month) + "." + year);
                }else if (String.valueOf(month).length() == 1)
                {
                    date = (day + ".0" + String.valueOf(month) + "." + year);
                }else {
                    date = ("0" + day + ".0" + String.valueOf(month) + "." + year);
                }
                data.setText(date);
            }
        };

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ChangesActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3d7894")));
        changedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.changedBtn){

                    String lastname = surname.getText().toString();
                    String Name = name.getText().toString();
                    String middleName = name_of_father.getText().toString();
                    String birhtday = data.getText().toString();
                    String SNILS = snils.getText().toString();
                    String Phone = phone.getText().toString();
                    String seria_oms = polis.getText().toString();
                    String nomer_oms = polis_number.getText().toString();

                    User user = new User(birhtday, email.getText().toString(), Name, lastname, middleName, nomer_oms, Phone,
                            seria_oms, SNILS, "patient");

                    if (!email.getText().toString().equals(""))
                    {
                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email.getText().toString());
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(user.getEmail());
                    }

                    if (!data.getText().toString().equals(""))
                    {
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("birhday").setValue(user.getBirhday());
                    }

                    if (!name.getText().toString().equals(""))
                    {
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("firstname").setValue(user.getFirstname());
                    }

                    if (!surname.getText().toString().equals(""))
                    {
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("lastname").setValue(user.getLastname());
                    }

                    if (!name_of_father.getText().toString().equals(""))
                    {
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("middlename").setValue(user.getMiddlename());
                    }

                    if (!polis.getText().toString().equals(""))
                    {
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("seriaOMS").setValue(user.getSeriaOMS());
                    }

                    if (!polis_number.getText().toString().equals(""))
                    {
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nomerOMS").setValue(user.getNomerOMS());
                    }

                    if (!phone.getText().toString().equals(""))
                    {
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone").setValue(user.getPhone());
                    }

                    if (!snils.getText().toString().equals(""))
                    {
                        mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snils").setValue(user.getPhone());
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}