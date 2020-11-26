package com.example.iccet2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TakeNoteActivity extends AppCompatActivity {

    private EditText surname, name, name_of_father, data, snils, email, password, phone, polis, polis_number;
    private Button registration_btn;

    private ArrayList countries;
    private ArrayAdapter adapterForSpinner;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_note);

        init();//вызов метода

        countries = new ArrayList<>();
        countries.add("Московская область");
        countries.add("Калининградская область");

        adapterForSpinner = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, countries);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterForSpinner);

        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.registration_btn){
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                }
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
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        polis = findViewById(R.id.polis);
        polis_number = findViewById(R.id.polis_number);

        registration_btn = findViewById(R.id.registration_btn);
    }
}