package com.example.iccet2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

public class RegistraitActivity extends AppCompatActivity {
    private EditText surname, name, name_of_father, data, snils, email, password, phone;
    private Button btnRegistrit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrait);

        init();

        btnRegistrit.setOnClickListener(BTNs);
    }

    View.OnClickListener BTNs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.registration_btn){
                Intent intent = new Intent(getApplicationContext(), TakeNoteActivity.class);
                startActivity(intent);
            }
        }
    };

    private void init(){
        surname = findViewById(R.id.surname);
        name = findViewById(R.id.name);
        name_of_father = findViewById(R.id.name_of_father);
        data = findViewById(R.id.data);
        snils = findViewById(R.id.snils);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);

        btnRegistrit = findViewById(R.id.registration_btn);
    }
}