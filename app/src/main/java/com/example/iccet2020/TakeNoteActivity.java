package com.example.iccet2020;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
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

public class TakeNoteActivity extends AppCompatActivity {

    private EditText surname, name, name_of_father, data, snils, email, phone, polis, polis_number;
    private Button registration_btn;

    private ArrayList countries;
    private ArrayAdapter adapterForSpinner;
    private Spinner spinner;
    private String userID;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_note);

        init();//вызов метода

        countries = new ArrayList<>();
        countries.add("Педиатр");
        countries.add("Психолог");
        countries.add("Лор");
        countries.add("Акушер-гинеколог");
        countries.add("Аллерголог-иммунолог");
        countries.add("Аллерголог");
        countries.add("Хирург");

        adapterForSpinner = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, countries);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterForSpinner);

        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddShedule.class);
                startActivity(intent);
            }
        });
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

        registration_btn = findViewById(R.id.registration_btn);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
