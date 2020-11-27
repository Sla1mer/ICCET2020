package com.example.iccet2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegistraitActivity extends AppCompatActivity {

    private EditText surname, name, name_of_father, data, snils, email, password, phone, seriaOMS, nomerOMS;
    private Button btnRegistrit;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";
    private FirebaseAuth mAuth;
    private static final String TAG = "mAuth";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrait);
        init();

        btnRegistrit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser(email.getText().toString(), password.getText().toString());
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
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

                DatePickerDialog dialog = new DatePickerDialog(RegistraitActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }


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
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mAuth = FirebaseAuth.getInstance();
        seriaOMS = findViewById(R.id.seriaOMS);
        nomerOMS = findViewById(R.id.nomerOMS);
    }

    // Создание нового пользователя
    private void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegistraitActivity.this, "Авторизация успешна", Toast.LENGTH_LONG).show();

                    String lastname = surname.getText().toString();
                    String Name = name.getText().toString();
                    String middleName = name_of_father.getText().toString();
                    String birhtday = data.getText().toString();
                    String SNILS = snils.getText().toString();
                    String Phone = phone.getText().toString();
                    String seria_oms = seriaOMS.getText().toString();
                    String nomer_oms = nomerOMS.getText().toString();

                    User user = new User(lastname, Name, middleName, birhtday, SNILS, email, Phone,
                            seria_oms, nomer_oms);
                    mDataBase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                    Intent intent = new Intent(getApplicationContext(), TakeNoteActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegistraitActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}