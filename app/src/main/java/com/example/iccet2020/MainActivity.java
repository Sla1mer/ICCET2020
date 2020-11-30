package com.example.iccet2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private MaterialButton signinBnt;
    private MaterialButton registrationBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        init();

        signinBnt.setOnClickListener(BTNs);
        registrationBtn.setOnClickListener(BTNs);
    }

    View.OnClickListener BTNs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case (R.id.registration):
                    Intent intent_For_registration = new Intent(getApplicationContext(), RegistraitActivity.class);
                    startActivity(intent_For_registration);
                    break;
                case (R.id.sign_in):
                    Intent intent_for_signIn = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent_for_signIn);
                    break;

            }
        }
    };

    // Инициализация элементов интерфейса
    private void init()
    {
        signinBnt = findViewById(R.id.sign_in);
        registrationBtn = findViewById(R.id.registration);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(getApplicationContext(), DoctorActivity.class);
            startActivity(intent);
        }
    }
}